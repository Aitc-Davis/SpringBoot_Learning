package davis.learn.springboot.RestAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(PetService.class)
class PetServiceTest {

	@Autowired
	PetService petService;

	@Autowired
	MockRestServiceServer mockServer;

	@Test
	void searchPetFromSwagger() {

		// 模擬 Server 處理
		mockServer.expect(requestTo("https://petstore.swagger.io/v2/pet/55688"))
				.andRespond(withSuccess(
						new ClassPathResource("json/pet_55688.json"),
						MediaType.APPLICATION_JSON)
				);

		Pet pet = petService.searchPetFromSwagger(55688);

		assertEquals(55688, pet.getId());
		assertEquals("PuiPui", pet.getName());
	}
}