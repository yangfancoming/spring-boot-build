

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.ResolvableType;

/**
 * Base class for configurations that can combine source beans using a
 * {@link CompositeHealthIndicator}.
 *
 * @param <H> the health indicator type
 * @param <S> the bean source type
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public abstract class CompositeHealthIndicatorConfiguration<H extends HealthIndicator, S> {

	@Autowired
	private HealthAggregator healthAggregator;

	protected HealthIndicator createHealthIndicator(Map<String, S> beans) {
		if (beans.size() == 1) {
			return createHealthIndicator(beans.values().iterator().next());
		}
		CompositeHealthIndicator composite = new CompositeHealthIndicator(
				this.healthAggregator);
		beans.forEach((name, source) -> composite.addHealthIndicator(name,
				createHealthIndicator(source)));
		return composite;
	}

	@SuppressWarnings("unchecked")
	protected H createHealthIndicator(S source) {
		Class<?>[] generics = ResolvableType
				.forClass(CompositeHealthIndicatorConfiguration.class, getClass())
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
