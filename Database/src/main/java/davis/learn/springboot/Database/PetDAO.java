package davis.learn.springboot.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class PetDAO {
	private static final Logger LOG = LoggerFactory.getLogger(DemoRestController.class);

	private JdbcTemplate jdbc;

	public PetDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	public boolean addPet(String id, String name) {
		String sql = "insert into pet (id, petname) values (?, ?)";
		try {
			int rows = jdbc.update(sql, id, name);
			LOG.info("Insert {} rows to Table[Pet]", rows);
			return rows > 0;
		}
		catch (DataAccessException e) {
			return false;
		}
	}

	public Map<String, Object> findPet(String petId) {
		String sql = "select * from pet where id = ?";
		List<Map<String, Object>> result = jdbc.queryForList(sql, petId);
		LOG.info("Select Result: {} ", result);
		return result.isEmpty() ? null : result.get(0);
	}
}
