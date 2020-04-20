

package org.springframework.boot.actuate.autoconfigure.flyway;

import org.flywaydb.core.Flyway;
import org.junit.Test;

import org.springframework.boot.actuate.flyway.FlywayEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FlywayEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class FlywayEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(FlywayEndpointAutoConfiguration.class))
			.withUserConfiguration(FlywayConfiguration.class);

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(FlywayEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.flyway.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(FlywayEndpoint.class));
	}

	@Configuration
	static class FlywayConfiguration {

		@Bean
		public Flyway flyway() {
			return mock(Flyway.class);
		}

	}

}
