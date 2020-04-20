

package org.springframework.boot.actuate.autoconfigure.beans;

import org.junit.Test;

import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BeansEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class BeansEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(BeansEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner
				.run((context) -> assertThat(context).hasSingleBean(BeansEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.beans.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(BeansEndpoint.class));
	}

}
