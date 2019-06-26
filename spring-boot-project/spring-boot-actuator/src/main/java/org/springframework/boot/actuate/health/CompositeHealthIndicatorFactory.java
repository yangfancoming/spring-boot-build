

package org.springframework.boot.actuate.health;

import java.util.Map;
import java.util.function.Function;

import org.springframework.util.Assert;

/**
 * Factory to create a {@link CompositeHealthIndicator}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class CompositeHealthIndicatorFactory {

	private final Function<String, String> healthIndicatorNameFactory;

	public CompositeHealthIndicatorFactory(
			Function<String, String> healthIndicatorNameFactory) {
		this.healthIndicatorNameFactory = healthIndicatorNameFactory;
	}

	public CompositeHealthIndicatorFactory() {
		this(new HealthIndicatorNameFactory());
	}

	/**
	 * Create a {@link CompositeHealthIndicator} based on the specified health indicators.
	 * @param healthAggregator the {@link HealthAggregator}
	 * @param healthIndicators the {@link HealthIndicator} instances mapped by name
	 * @return a {@link HealthIndicator} that delegates to the specified
	 * {@code healthIndicators}.
	 */
	public CompositeHealthIndicator createHealthIndicator(
			HealthAggregator healthAggregator,
			Map<String, HealthIndicator> healthIndicators) {
		Assert.notNull(healthAggregator, "HealthAggregator must not be null");
		Assert.notNull(healthIndicators, "HealthIndicators must not be null");
		CompositeHealthIndicator healthIndicator = new CompositeHealthIndicator(
				healthAggregator);
		for (Map.Entry<String, HealthIndicator> entry : healthIndicators.entrySet()) {
			String name = this.healthIndicatorNameFactory.apply(entry.getKey());
			healthIndicator.addHealthIndicator(name, entry.getValue());
		}
		return healthIndicator;
	}

}
