

package org.springframework.boot.actuate.autoconfigure.health;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.actuate.health.CompositeReactiveHealthIndicator;
import org.springframework.boot.actuate.health.CompositeReactiveHealthIndicatorFactory;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.ApplicationContext;

/**
 * Creates a {@link CompositeReactiveHealthIndicator} from beans in the
 * {@link ApplicationContext}.
 *
 * @author Phillip Webb
 */
final class HealthIndicatorBeansReactiveComposite {

	private HealthIndicatorBeansReactiveComposite() {
	}

	public static ReactiveHealthIndicator get(ApplicationContext applicationContext) {
		HealthAggregator healthAggregator = getHealthAggregator(applicationContext);
		return new CompositeReactiveHealthIndicatorFactory()
				.createReactiveHealthIndicator(healthAggregator,
						applicationContext.getBeansOfType(ReactiveHealthIndicator.class),
						applicationContext.getBeansOfType(HealthIndicator.class));
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

}
