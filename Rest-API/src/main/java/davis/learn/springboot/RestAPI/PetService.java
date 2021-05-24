package davis.learn.springboot.RestAPI;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class PetService {

	private final RestTemplate rest;

	public PetService(RestTemplateBuilder builder) {
		this.rest = builder.build();
	}

	public Pet searchPetFromSwagger(int petId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ACCEPT, APPLICATION_JSON_VALUE);
		HttpEntity entity = new HttpEntity(headers);

		try {
			ResponseEntity<Pet> response = rest.exchange(
					"https://petstore.swagger.io/v2/pet/{petId}",
					GET, entity, Pet.class, petId
			);
			return response.getBody();
		}
		catch (HttpStatusCodeException e) {
			return null;
		}
	}
}
