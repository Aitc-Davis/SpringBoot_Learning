package davis.learn.springboot.RestAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CusHealthIndicator implements HealthIndicator {
	private static final Logger LOG = LoggerFactory.getLogger(CusHealthIndicator.class);

	@Override
	public Health health() {
		long timestamp = System.currentTimeMillis();
		Health.Builder up = Health.up().withDetail("timestamp", timestamp);
		if (timestamp % 2 == 0) {
			return up.withException(new Exception("Timestamp is a multiple of two")).build();
		}
		else {
			return up.build();
		}
	}
}