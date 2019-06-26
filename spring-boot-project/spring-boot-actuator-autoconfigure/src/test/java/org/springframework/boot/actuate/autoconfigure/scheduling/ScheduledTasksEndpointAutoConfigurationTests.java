

package org.springframework.boot.actuate.autoconfigure.scheduling;

import java.util.Collections;

import org.junit.Test;

import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ScheduledTasksEndpointAutoConfiguration}.
 *
 * @author Andy Wilkinson
 */
public class ScheduledTasksEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(ScheduledTasksEndpointAutoConfiguration.class));

	@Test
	public void endpointIsAutoConfigured() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(ScheduledTasksEndpoint.class));
	}

	@Test
	public void endpointCanBeDisabled() {
		this.contextRunner
				.withPropertyValues("management.endpoint.scheduledtasks.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ScheduledTasksEndpoint.class));
	}

	@Test
	public void endpointBacksOffWhenUserProvidedEndpointIsPresent() {
		this.contextRunner.withUserConfiguration(CustomEndpointConfiguration.class)
				.run((context) -> assertThat(context)
						.hasSingleBean(ScheduledTasksEndpoint.class)
						.hasBean("customEndpoint"));
	}

	private static class CustomEndpointConfiguration {

		@Bean
		public CustomEndpoint customEndpoint() {
			return new CustomEndpoint();
		}

	}

	private static final class CustomEndpoint extends ScheduledTasksEndpoint {

		private CustomEndpoint() {
			super(Collections.emptyList());
		}

	}

}
