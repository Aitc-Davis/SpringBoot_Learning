package davis.learn.springboot.RestAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExpControllerAdvice {
	private static final Logger LOG = LoggerFactory.getLogger(ExpControllerAdvice.class);

	@ExceptionHandler({AAAException.class})
	public final void handleException(AAAException ex) {
		LOG.error("Handle {} : {}", ex.getClass().getName(), ex.getMessage());
	}
}