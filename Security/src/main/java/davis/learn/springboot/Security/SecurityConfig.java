package davis.learn.springboot.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 參考文件：https://docs.spring.io/spring-security/site/docs/5.4.x/reference/html5/
 */
//@EnableJdbcHttpSession
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Default Rounds = 10
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public AuthenticationSuccessHandler authSuccHandler() {
		return new AuthSuccHandler();
	}

	@Bean
	public AuthenticationFailureHandler authFailHandler() {
		return new AuthFailHandler();
	}

	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();

		// 多 Session 控制
		ConcurrentSessionControlAuthenticationStrategy authenticationStrategy
				= new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
		authenticationStrategy.setMaximumSessions(1);
		authenticationStrategy.setExceptionIfMaximumExceeded(true);
		delegateStrategies.add(authenticationStrategy);

		// 防禦 Session 固定攻擊
		delegateStrategies.add(new SessionFixationProtectionStrategy());
		// Session 註冊
		delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));

		return new CompositeSessionAuthenticationStrategy(delegateStrategies);
	}

	@Bean
	public InvalidSessionStrategy invalidSessionStrategy() {
		return new InvalidSessionStrategy() {
			private final Logger LOG = LoggerFactory.getLogger(InvalidSessionStrategy.class);

			@Override
			public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				LOG.error("Invalid Session: IP[{}], URI[{}]", request.getRemoteHost(), request.getRequestURI());
			}
		};
	}

	// GenericFilterBean 類型不建議註冊成 Bean
	// 因為 SpringBoot 在自動生成 Filter Chain 時，會自動添加 GenericFilterBean 類型的 Bean
	// 這不便於在 HttpSecurity 添加並控制 Filter 的順序
	public GenericFilterBean cusSecurityFilter() {
		return new GenericFilterBean() {
			private final Logger LOG = LoggerFactory.getLogger("CusSecurityFilter");

			@Override
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
				String url = ((HttpServletRequest) servletRequest).getRequestURI();
				String username = servletRequest.getParameter("username");
				LOG.info("URL:[{}] ; Username:[{}]", url, username);
				if (url.equals("/login") && username.equals("davis")) {
					((HttpServletResponse) servletResponse).sendRedirect("/logout");
				}
				else {
					// 有串上 Filter Chain 才會繼續執行下一個 Filter
					filterChain.doFilter(servletRequest, servletResponse);
				}
			}
		};
	}

	// 帳戶授權管理配置
	@Override

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("davis").password(passwordEncoder().encode("0000")).roles("TestMember")
				.and()
				.withUser("admin").password(passwordEncoder().encode("0000")).roles("Admin")

//				.userDetailsService(username -> new User(
//						username,
//						SecurityConfig.this.passwordEncoder().encode("0000"), // BCrypt(10)
//						true,
//						true,
//						true,
//						true,
//						username.startsWith("adm") ?
//								Arrays.asList(
//										new SimpleGrantedAuthority("ROLE_Admin"),
//										new SimpleGrantedAuthority("ROLE_TestMember")
//								) :
//								Arrays.asList(
//										new SimpleGrantedAuthority("ROLE_TestMember")
//								)
//				))
//				.passwordEncoder(passwordEncoder())
		;
	}

	// 全域安全性配置
	@Override
	public void configure(WebSecurity web) {
		web
				.ignoring()
				.antMatchers("/css/*", "/js/*", "/images/*", "/favicon.ico");
	}

	// HTTP安全性配置
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf()
				// 前後分離時，CSRF Token 需要 JavaScript 協助放置，因次不能對 _csrf 啟用 HTTP Only
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

				.and()
				// Request 授權檢查
				.authorizeRequests()
				// 任何人都能訪問
				.antMatchers("/login.html").permitAll()
				.antMatchers("/petname", "/pet.html").permitAll()
//				.antMatchers("/today").hasRole("Admin")
				// 其餘的需要授權
				.anyRequest().authenticated()

				.and()
				.formLogin()
				// 登入頁面
				.loginPage("/login.html")
				// 提交登入 Submit 的 URL
				.loginProcessingUrl("/login")
				// 登入成功後處理
				.successHandler(authSuccHandler())
//				// 登入成功重導 URL ( 這會建立 successHandler，同時只能有一個 successHandler )
//				.defaultSuccessUrl("/today", true)
				// 登入失敗後處理
				.failureHandler(authFailHandler())

				.and()
				.logout()
				// 收到請求登出
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				// 登出成功後重導
				.logoutSuccessUrl("/login.html")
				// 清除授權
				.clearAuthentication(true)
				// 清除 Session
				.invalidateHttpSession(true)
				// 清除 Cookies
				.deleteCookies("JSESSIONID", "XSRF-TOKEN")

				.and()
				// Session 設定
				.sessionManagement()
				// Session 產生策略
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				// 授權驗證處理
				.sessionAuthenticationStrategy(sessionAuthenticationStrategy())
				// Session 無效處理
				.invalidSessionStrategy(invalidSessionStrategy())

//				.and()
//				// 添加其他過濾功能
//				.addFilterBefore(cusSecurityFilter(), UsernamePasswordAuthenticationFilter.class)
		;
	}
}