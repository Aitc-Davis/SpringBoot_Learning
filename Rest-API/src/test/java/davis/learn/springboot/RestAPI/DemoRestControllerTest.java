package davis.learn.springboot.RestAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	Echo echo;

	@Test
	void today() throws Exception {
		// 模擬 Client 發送，並斷言回應
		mockMvc.perform(get("/demo/today")
				.accept(TEXT_PLAIN))
//				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void echoTextAsJson() throws Exception {

		// Mockito 定義 method 處理
		when(echo.echoTextAsJson(matches(".*")))
				.thenAnswer(in -> {
					System.out.println("{\"ECHO\" :\"" + in.getArgument(0) + "\"}");
					return "{\"ECHO\" :\"" + in.getArgument(0) + "\"}";
				});

		mockMvc.perform(post("/demo/echo/Davis")
				.contentType(TEXT_PLAIN)
				.content("abcde")
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"ECHO\" :\"abcde\"}"));
	}
}