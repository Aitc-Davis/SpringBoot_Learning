package davis.learn.springboot.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


public class AuthSuccHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final Logger LOG = LoggerFactory.getLogger(AuthSuccHandler.class);

	public AuthSuccHandler(String defaultTargeyUrl) {
		super(defaultTargeyUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		Set<String> authSet = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		String username = auth.getName();
		LOG.info("[{}] has authorisations: {} ; Session: [{}]", username, authSet, req.getSession().getId());
		super.onAuthenticationSuccess(req, resp, auth);
	}
}