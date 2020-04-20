

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 */
public class HealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(HealthIndicatorAutoConfiguration.class));

	@Test
	public void runWhenNoOtherIndicatorsShouldCreateDefaultApplicationHealthIndicator() {
		this.contextRunner
				.run((context) -> assertThat(context).getBean(HealthIndicator.class)
						.isInstanceOf(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenHasDefinedIndicatorShouldNotCreateDefaultApplicationHealthIndicator() {
		this.contextRunner.withUserConfiguration(CustomHealthIndicatorConfiguration.class)
				.run((context) -> assertThat(context).getBean(HealthIndicator.class)
						.isInstanceOf(CustomHealthIndicator.class));
	}

	@Test
	public void runWhenHasDefaultsDisabledAndNoSingleIndicatorEnabledShouldCreateDefaultApplicationHealthIndicator() {
		this.contextRunner.withUserConfiguration(CustomHealthIndicatorConfiguration.class)
				.withPropertyValues("management.health.defaults.enabled:false")
				.run((context) -> assertThat(context).getBean(HealthIndicator.class)
						.isInstanceOf(ApplicationHealthIndicator.class));

	}

	@Test
	public void runWhenHasDefaultsDisabledAndSingleIndicatorEnabledShouldCreateEnabledIndicator() {
		this.contextRunner.withUserConfiguration(CustomHealthIndicatorConfiguration.class)
				.withPropertyValues("management.health.defaults.enabled:false",
						"management.health.custom.enabled:true")
				.run((context) -> assertThat(context).getBean(HealthIndicator.class)
						.isInstanceOf(CustomHealthIndicator.class));

	}

	@Test
	public void runShouldCreateOrderedHealthAggregator() {
		this.contextRunner
				.run((context) -> assertThat(context).getBean(HealthAggregator.class)
						.isInstanceOf(OrderedHealthAggregator.class));
	}

	@Test
	public void runWhenHasCustomOrderPropertyShouldCreateOrderedHealthAggregator() {
		this.contextRunner.withPropertyValues("management.health.status.order:UP,DOWN")
				.run((context) -> {
					OrderedHealthAggregator aggregator = context
							.getBean(OrderedHealthAggregator.class);
					Map<String, Health> healths = new LinkedHashMap<>();
					healths.put("foo", Health.up().build());
					healths.put("bar", Health.down().build());
					Health aggregate = aggregator.aggregate(healths);
					assertThat(aggregate.getStatus()).isEqualTo(Status.UP);
				});
	}

	@Test
	public void runWhenHasCustomHealthAggregatorShouldNotCreateOrderedHealthAggregator() {
		this.contextRunner
				.withUserConfiguration(CustomHealthAggregatorConfiguration.class)
				.run((context) -> assertThat(context).getBean(HealthAggregator.class)
						.isNotInstanceOf(OrderedHealthAggregator.class));
	}

	@Configuration
	static class CustomHealthIndicatorConfiguration {

		@Bean
		@ConditionalOnEnabledHealthIndicator("custom")
		public HealthIndicator customHealthIndicator() {
			return new CustomHealthIndicator();
		}

	}

	static class CustomHealthIndicator implements HealthIndicator {

		@Override
		public Health health() {
			return Health.down().build();
		}

	}

	@Configuration
	static class CustomHealthAggregatorConfiguration {

		@Bean
		public HealthAggregator healthAggregator() {
			return (healths) -> Health.down().build();
		}

	}

}
