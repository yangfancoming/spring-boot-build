

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.CompositeHealthIndicatorFactory;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * Creates a {@link CompositeHealthIndicator} from beans in the
 * {@link ApplicationContext}.
 *
 * @author Phillip Webb
 */
final class HealthIndicatorBeansComposite {

	private HealthIndicatorBeansComposite() {
	}

	public static HealthIndicator get(ApplicationContext applicationContext) {
		HealthAggregator healthAggregator = getHealthAggregator(applicationContext);
		Map<String, HealthIndicator> indicators = new LinkedHashMap<>();
		indicators.putAll(applicationContext.getBeansOfType(HealthIndicator.class));
		if (ClassUtils.isPresent("reactor.core.publisher.Flux", null)) {
			new ReactiveHealthIndicators().get(applicationContext)
					.forEach(indicators::putIfAbsent);
		}
		CompositeHealthIndicatorFactory factory = new CompositeHealthIndicatorFactory();
		return factory.createHealthIndicator(healthAggregator, indicators);
	}

	private static HealthAggregator getHealthAggregator(
			ApplicationContext applicationContext) {
		try {
			return applicationContext.getBean(HealthAggregator.class);
		}
		catch (NoSuchBeanDefinitionException ex) {
			return new OrderedHealthAggregator();
		}
	}

	private static class ReactiveHealthIndicators {

		public Map<String, HealthIndicator> get(ApplicationContext applicationContext) {
			Map<String, HealthIndicator> indicators = new LinkedHashMap<>();
			applicationContext.getBeansOfType(ReactiveHealthIndicator.class)
					.forEach((name, indicator) -> indicators.put(name, adapt(indicator)));
			return indicators;
		}

		private HealthIndicator adapt(ReactiveHealthIndicator indicator) {
			return () -> indicator.health().block();
		}

	}

}
