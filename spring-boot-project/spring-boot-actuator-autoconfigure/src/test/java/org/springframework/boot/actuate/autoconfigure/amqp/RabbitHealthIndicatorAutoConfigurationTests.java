

package org.springframework.boot.actuate.autoconfigure.amqp;

import org.junit.Test;

import org.springframework.boot.actuate.amqp.RabbitHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RabbitHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class RabbitHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(RabbitAutoConfiguration.class,
					RabbitHealthIndicatorAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(RabbitHealthIndicator.class)
				.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.rabbit.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(RabbitHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
