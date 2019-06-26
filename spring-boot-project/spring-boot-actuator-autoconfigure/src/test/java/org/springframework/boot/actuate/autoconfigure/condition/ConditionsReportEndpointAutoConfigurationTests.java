

package org.springframework.boot.actuate.autoconfigure.condition;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionsReportEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class ConditionsReportEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations
					.of(ConditionsReportEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(ConditionsReportEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("management.endpoint.conditions.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ConditionsReportEndpoint.class));
	}

}
