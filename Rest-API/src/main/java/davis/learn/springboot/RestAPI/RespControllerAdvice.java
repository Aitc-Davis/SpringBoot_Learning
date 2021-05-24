package davis.learn.springboot.RestAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class RespControllerAdvice implements ResponseBodyAdvice<Object> {
	private static final Logger LOG = LoggerFactory.getLogger(RespControllerAdvice.class);

	@Override
	public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> converterType) {
		LOG.info("=== Supports : {}", mp.getParameterType().getName());
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		LOG.info("=== Before Body Write");
		return body;
//		return "!!! " + body;
	}
}