

package org.springframework.boot.actuate.autoconfigure.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.Test;

import org.springframework.boot.actuate.liquibase.LiquibaseEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link LiquibaseEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class LiquibaseEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(LiquibaseEndpointAutoConfiguration.class))
			.withUserConfiguration(LiquibaseConfiguration.class);

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(LiquibaseEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("management.endpoint.liquibase.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(LiquibaseEndpoint.class));
	}

	@Configuration
	static class LiquibaseConfiguration {

		@Bean
		public SpringLiquibase liquibase() {
			return mock(SpringLiquibase.class);
		}

	}

}
