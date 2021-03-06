package davis.learn.springboot.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
	private static final Logger LOG = LoggerFactory.getLogger(AuthFailHandler.class);

	public AuthFailHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
		LOG.info("{} login failed, {}", req.getParameter("username"), exception.getClass().getSimpleName() + ": " + exception.getMessage());
		saveException(req, exception);
		super.onAuthenticationFailure(req, resp, exception);
	}
}