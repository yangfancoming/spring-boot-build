

package org.springframework.boot.actuate.autoconfigure.influx;

import org.influxdb.InfluxDB;
import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.influx.InfluxDbHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link InfluxDbHealthIndicatorAutoConfiguration}.
 *
 * @author Eddú Meléndez
 */
public class InfluxDbHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(InfluxDbConfiguration.class).withConfiguration(
					AutoConfigurations.of(InfluxDbHealthIndicatorAutoConfiguration.class,
							HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(InfluxDbHealthIndicator.class)
				.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.influxdb.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(InfluxDbHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

	@Configuration
	static class InfluxDbConfiguration {

		@Bean
		public InfluxDB influxdb() {
			return mock(InfluxDB.class);
		}

	}

}
