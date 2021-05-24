package davis.learn.springboot.RestAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Echo {
	private final ObjectMapper objMapper = new ObjectMapper();

	public String echoJson(String json) throws JsonProcessingException {
		ObjectNode node = objMapper.readValue(json, ObjectNode.class);
		node.put("ECHO", Instant.now().toString());
		return objMapper.writeValueAsString(node);
	}

	public String echoTextAsJson(String text) throws JsonProcessingException {
		ObjectNode node = objMapper.createObjectNode();
		node.put("ECHO", text);
		return objMapper.writeValueAsString(node);
	}
}
