package davis.learn.springboot.RestAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/demo")
public class DemoRestController {
	private static final Logger LOG = LoggerFactory.getLogger(DemoRestController.class);

	@Autowired
	PetDAO petDAO;

	/**
	 * Post API，利用 {@link PetDAO} 串接後面 Database
	 */
	@PostMapping(value = "/pet",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity addPet(@RequestBody Map<String, String> pet) {
		LOG.info("Add Pet : {}", pet);
		String id = pet.getOrDefault("id", "");
		String name = pet.getOrDefault("petname", "");
		if (id.isBlank() || name.isBlank()) {
			return ResponseEntity.badRequest().build();
		}
		else if (petDAO.addPet(id, name)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}
	}

	/**
	 * Get API，利用 {@link PetDAO} 串接後面 Database
	 */
	@GetMapping(value = "/pet/{petId}",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity findPet(@PathVariable String petId) {
		LOG.info("Find Pet ID:[{}]", petId);
		Map<String, Object> pet = petDAO.findPet(petId);
		return pet == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(pet);
	}
}
