

package org.springframework.boot.actuate.autoconfigure.management;

import org.junit.Test;

import org.springframework.boot.actuate.management.ThreadDumpEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ThreadDumpEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class ThreadDumpEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(ThreadDumpEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(ThreadDumpEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("management.endpoint.threaddump.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ThreadDumpEndpoint.class));
	}

}
