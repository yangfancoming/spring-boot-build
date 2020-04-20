

package org.springframework.boot.actuate.autoconfigure.info;

import org.junit.Test;

import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InfoEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class InfoEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(InfoEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.shutdown.enabled:true")
				.run((context) -> assertThat(context).hasSingleBean(InfoEndpoint.class));
	}

	@Test
	public void runShouldHaveEndpointBeanEvenIfDefaultIsDisabled() {
		// FIXME
		this.contextRunner.withPropertyValues("management.endpoint.default.enabled:false")
				.run((context) -> assertThat(context).hasSingleBean(InfoEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.info.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(InfoEndpoint.class));
	}

}
