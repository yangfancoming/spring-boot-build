

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.core.ResolvableType;

/**
 * Reactive variant of {@link CompositeHealthIndicatorConfiguration}.
 *
 * @param <H> the health indicator type
 * @param <S> the bean source type
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public abstract class CompositeReactiveHealthIndicatorConfiguration<H extends ReactiveHealthIndicator, S> {

	@Autowired
	private HealthAggregator healthAggregator;

	protected ReactiveHealthIndicator createHealthIndicator(Map<String, S> beans) {
		if (beans.size() == 1) {
			return createHealthIndicator(beans.values().iterator().next());
		}
		CompositeReactiveHealthIndicator composite = new CompositeReactiveHealthIndicator(
				this.healthAggregator);
		beans.forEach((name, source) -> composite.addHealthIndicator(name,
				createHealthIndicator(source)));
		return composite;
	}

	@SuppressWarnings("unchecked")
	protected H createHealthIndicator(S source) {
		Class<?>[] generics = ResolvableType
				.forClass(CompositeReactiveHealthIndicatorConfiguration.class, getClass())
				.resolveGenerics();
		Class<H> indicatorClass = (Class<H>) generics[0];
		Class<S> sourceClass = (Class<S>) generics[1];
		try {
			return indicatorClass.getConstructor(sourceClass).newInstance(source);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Unable to create indicator " + indicatorClass
					+ " for source " + sourceClass, ex);
		}
	}

}
