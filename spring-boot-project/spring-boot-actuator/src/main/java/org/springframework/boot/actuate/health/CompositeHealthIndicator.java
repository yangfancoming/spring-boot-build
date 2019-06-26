

package org.springframework.boot.actuate.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * {@link HealthIndicator} that returns health indications from all registered delegates.
 *
 * @author Tyler J. Frederick
 * @author Phillip Webb
 * @author Christian Dupuis
 * @since 1.1.0
 */
public class CompositeHealthIndicator implements HealthIndicator {

	private final Map<String, HealthIndicator> indicators;

	private final HealthAggregator healthAggregator;

	/**
	 * Create a new {@link CompositeHealthIndicator}.
	 * @param healthAggregator the health aggregator
	 */
	public CompositeHealthIndicator(HealthAggregator healthAggregator) {
		this(healthAggregator, new LinkedHashMap<>());
	}

	/**
	 * Create a new {@link CompositeHealthIndicator} from the specified indicators.
	 * @param healthAggregator the health aggregator
	 * @param indicators a map of {@link HealthIndicator}s with the key being used as an
	 * indicator name.
	 */
	public CompositeHealthIndicator(HealthAggregator healthAggregator,
			Map<String, HealthIndicator> indicators) {
		Assert.notNull(healthAggregator, "HealthAggregator must not be null");
		Assert.notNull(indicators, "Indicators must not be null");
		this.indicators = new LinkedHashMap<>(indicators);
		this.healthAggregator = healthAggregator;
	}

	public void addHealthIndicator(String name, HealthIndicator indicator) {
		this.indicators.put(name, indicator);
	}

	@Override
	public Health health() {
		Map<String, Health> healths = new LinkedHashMap<>();
		for (Map.Entry<String, HealthIndicator> entry : this.indicators.entrySet()) {
			healths.put(entry.getKey(), entry.getValue().health());
		}
		return this.healthAggregator.aggregate(healths);
	}

}
