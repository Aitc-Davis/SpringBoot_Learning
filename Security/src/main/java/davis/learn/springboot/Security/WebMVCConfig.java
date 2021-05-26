package davis.learn.springboot.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Autowired
	CusHandlerInterceptor cusHandlerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 為指定Path的添加攔截器 (Log、監控、...)
		registry.addInterceptor(cusHandlerInterceptor)
				.addPathPatterns("/**");
	}
}
