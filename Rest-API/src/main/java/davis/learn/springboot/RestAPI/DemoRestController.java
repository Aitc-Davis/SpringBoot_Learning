package davis.learn.springboot.RestAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController()
@RequestMapping("/demo")
public class DemoRestController {
	private static final Logger LOG = LoggerFactory.getLogger(DemoRestController.class);

	@Autowired
	Echo echo;

	@Autowired
	PetService petService;


	/**
	 * 一個簡單的 Get API
	 */
	@GetMapping(value = "/today",
			produces = TEXT_PLAIN_VALUE
	)
	public ResponseEntity today(HttpSession session) {
		LOG.info("Session:[{}]", session.getId());
		LocalDateTime now = LocalDateTime.now();
		String dt = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LOG.info("Today:[{}]", dt);
		return ResponseEntity.ok().body(dt);
	}


	/**
	 * 一個簡單的 Post API，只接受 application/json
	 */
	@PostMapping(value = "/echo/{name}",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity echoJson(@RequestBody String msg, @PathVariable String name) {
		LOG.info("{} call EchoJson !!", name);
		try {
			return ResponseEntity.ok()
					.body(echo.echoJson(msg));
		}
		catch (JsonProcessingException e) {
			LOG.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Service Trouble!!");
		}
	}


	/**
	 * 相同的 Post API 路徑，但只接受 text/plain
	 */
	@PostMapping(value = "/echo/{name}",
			consumes = TEXT_PLAIN_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity echoTextAsJson(@RequestBody String msg, @PathVariable String name) {
		LOG.info("{} call EchoTextAsJson !!", name);
		try {
			return ResponseEntity.ok()
					.body(echo.echoTextAsJson(msg));
		}
		catch (JsonProcessingException e) {
			LOG.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Service Trouble!!");
		}
	}


	/**
	 * Get API，利用 {@link PetService} 後面串接其他服務
	 */
	@GetMapping(value = "/pet/{petId}",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity pet(@PathVariable Integer petId) {
		LOG.info("Pet ID:[{}]", petId);
		Pet pet = petService.searchPetFromSwagger(petId);
		return pet == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(pet);
	}


	/**
	 * 做 HTTP 302 的頁面重導
	 */
	@GetMapping(value = "/page")
	public ModelAndView page() {
		RedirectView view = new RedirectView("/pages/page01.html");
		return new ModelAndView(view);
	}


	/**
	 * 刻意拋出 Exception，觀察 @ExceptionHandler 如何處理
	 */
	@GetMapping(value = "/trouble")
	public ResponseEntity trouble(@RequestParam String flag) throws Exception {
		if (flag.startsWith("a")) {
			throw new AAAException("AAA");
		}
		else {
			throw new BBBException("BBB");
		}
	}
}
