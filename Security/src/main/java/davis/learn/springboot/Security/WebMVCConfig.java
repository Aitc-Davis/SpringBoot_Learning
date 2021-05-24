package davis.learn.springboot.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 為指定Path的添加攔截器 (Log、監控、...)
		registry.addInterceptor(new CusHandlerInterceptor())
				.addPathPatterns("/demo/**");
	}
}
