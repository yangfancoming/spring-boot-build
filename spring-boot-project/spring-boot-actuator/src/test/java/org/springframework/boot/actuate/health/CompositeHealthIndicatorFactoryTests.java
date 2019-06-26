

package org.springframework.boot.actuate.health;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CompositeHealthIndicatorFactory}.
 *
 * @author Phillip Webb
 * @author Christian Dupuis
 * @author Andy Wilkinson
 */
public class CompositeHealthIndicatorFactoryTests {

	@Test
	public void upAndUpIsAggregatedToUp() {
		Map<String, HealthIndicator> healthIndicators = new HashMap<>();
		healthIndicators.put("up", () -> new Health.Builder().status(Status.UP).build());
		healthIndicators.put("upAgain",
				() -> new Health.Builder().status(Status.UP).build());
		HealthIndicator healthIndicator = createHealthIndicator(healthIndicators);
		assertThat(healthIndicator.health().getStatus()).isEqualTo(Status.UP);
	}

	@Test
	public void upAndDownIsAggregatedToDown() {
		Map<String, HealthIndicator> healthIndicators = new HashMap<>();
		healthIndicators.put("up", () -> new Health.Builder().status(Status.UP).build());
		healthIndicators.put("down",
				() -> new Health.Builder().status(Status.DOWN).build());
		HealthIndicator healthIndicator = createHealthIndicator(healthIndicators);
		assertThat(healthIndicator.health().getStatus()).isEqualTo(Status.DOWN);
	}

	@Test
	public void unknownStatusMapsToUnknown() {
		Map<String, HealthIndicator> healthIndicators = new HashMap<>();
		healthIndicators.put("status", () -> new Health.Builder().status("FINE").build());
		HealthIndicator healthIndicator = createHealthIndicator(healthIndicators);
		assertThat(healthIndicator.health().getStatus()).isEqualTo(Status.UNKNOWN);
	}

	private HealthIndicator createHealthIndicator(
			Map<String, HealthIndicator> healthIndicators) {
		return new CompositeHealthIndicatorFactory()
				.createHealthIndicator(new OrderedHealthAggregator(), healthIndicators);
	}

}
