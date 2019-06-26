

package org.springframework.boot.actuate.health;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ApplicationHealthIndicator}.
 *
 * @author Phillip Webb
 */
public class ApplicationHealthIndicatorTests {

	@Test
	public void indicatesUp() {
		ApplicationHealthIndicator healthIndicator = new ApplicationHealthIndicator();
		assertThat(healthIndicator.health().getStatus()).isEqualTo(Status.UP);
	}

}
