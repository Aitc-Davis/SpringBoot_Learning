package davis.learn.springboot.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController()
@RequestMapping("/")
public class DemoRestController {
	private static final Logger LOG = LoggerFactory.getLogger(DemoRestController.class);

	/**
	 * 一個簡單的 Get API
	 */
	@GetMapping(value = "/today",
			produces = TEXT_PLAIN_VALUE
	)
	public ResponseEntity today(HttpSession session) {
		LocalDateTime now = LocalDateTime.now();
		String dt = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LOG.info("Today:[{}] ; Session:[{}]", dt, session.getId());
		return ResponseEntity.ok().body(dt);
	}
}
