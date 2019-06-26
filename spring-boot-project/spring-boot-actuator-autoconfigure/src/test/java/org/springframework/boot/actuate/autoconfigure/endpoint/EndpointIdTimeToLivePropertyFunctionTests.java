

package org.springframework.boot.actuate.autoconfigure.endpoint;

import java.util.function.Function;

import org.junit.Test;

import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EndpointIdTimeToLivePropertyFunction}.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
public class EndpointIdTimeToLivePropertyFunctionTests {

	private final MockEnvironment environment = new MockEnvironment();

	private final Function<String, Long> timeToLive = new EndpointIdTimeToLivePropertyFunction(
			this.environment);

	@Test
	public void defaultConfiguration() {
		Long result = this.timeToLive.apply("test");
		assertThat(result).isNull();
	}

	@Test
	public void userConfiguration() {
		this.environment.setProperty("management.endpoint.test.cache.time-to-live",
				"500");
		Long result = this.timeToLive.apply("test");
		assertThat(result).isEqualTo(500L);
	}

}
