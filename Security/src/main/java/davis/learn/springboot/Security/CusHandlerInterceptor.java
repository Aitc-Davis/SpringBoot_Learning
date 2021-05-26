package davis.learn.springboot.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CusHandlerInterceptor implements HandlerInterceptor {
	private static final Logger LOG = LoggerFactory.getLogger(CusHandlerInterceptor.class);

	// 攔截進入的電文，判斷是否繼續處理
	// 進入 Controller 前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOG.info("=== Cus Pre Handle : {}", request.getRequestURI());
//		response.setStatus(555);
//		return false;
		return true;
	}

	// 攔截渲染畫面的動作，渲染前要做那些處理：修改 ModelAndView
	// Controller 結束後
	// 不適合在此處調整 Response，建議使用 @RestControllerAdvice
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		LOG.info("=== Cus Post Handle : {}", request.getRequestURI());
	}

	// 渲染後的處理：資源清理等等
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		LOG.info("=== Cus After Completion : {}", request.getRequestURI());
	}
}
