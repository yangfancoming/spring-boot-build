

package org.springframework.boot.actuate.autoconfigure.context;

import org.junit.Test;

import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ShutdownEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class ShutdownEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(ShutdownEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.shutdown.enabled:true")
				.run((context) -> assertThat(context)
						.hasSingleBean(ShutdownEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("management.endpoint.shutdown.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ShutdownEndpoint.class));
	}

}
