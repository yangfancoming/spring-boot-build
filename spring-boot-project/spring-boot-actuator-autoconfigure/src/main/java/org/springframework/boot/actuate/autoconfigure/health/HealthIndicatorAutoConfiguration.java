

package org.springframework.boot.actuate.autoconfigure.health;

import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link HealthIndicator}s.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@EnableConfigurationProperties({ HealthIndicatorProperties.class })
public class HealthIndicatorAutoConfiguration {

	private final HealthIndicatorProperties properties;

	public HealthIndicatorAutoConfiguration(HealthIndicatorProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean({ HealthIndicator.class, ReactiveHealthIndicator.class })
	public ApplicationHealthIndicator applicationHealthIndicator() {
		return new ApplicationHealthIndicator();
	}

	@Bean
	@ConditionalOnMissingBean(HealthAggregator.class)
	public OrderedHealthAggregator healthAggregator() {
		OrderedHealthAggregator healthAggregator = new OrderedHealthAggregator();
		if (this.properties.getOrder() != null) {
			healthAggregator.setStatusOrder(this.properties.getOrder());
		}
		return healthAggregator;
	}

}
