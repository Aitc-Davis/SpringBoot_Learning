package davis.learn.springboot.Database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
class PetDAOTest {

	static private PetDAO petDAO;

	@Autowired
	JacksonTester<Map<String, Object>> jacksonTester;

	@BeforeAll
	static void init() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/table_pet.sql")
				.build();
		petDAO = new PetDAO(dataSource);
	}

	@Test
	void addPet() {
		boolean monkey = petDAO.addPet("12345", "MMMMonkey");
		assertFalse(monkey);
		boolean bird = petDAO.addPet("23456", "BBBBird");
		assertTrue(bird);
	}

	@Test
	void findPet() throws IOException {
		Map<String, Object> pet = petDAO.findPet("12345");
		System.out.println(pet);
		pet = pet.entrySet()
				.parallelStream()
				.collect(Collectors.toMap(e -> e.getKey().toLowerCase(), Map.Entry::getValue));
		assertThat(jacksonTester.write(pet))
				.isEqualToJson(new ClassPathResource("jdbc/12345_CCCCat.json"));
	}
}