

package org.springframework.boot.actuate.autoconfigure.system;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DiskSpaceHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class DiskSpaceHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(DiskSpaceHealthIndicatorAutoConfiguration.class,
							HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(DiskSpaceHealthIndicator.class)
				.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.diskspace.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(DiskSpaceHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
