

package org.springframework.boot.actuate.autoconfigure.metrics;

import java.util.List;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryConfiguration.MultipleNonPrimaryMeterRegistriesCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for a {@link CompositeMeterRegistry}.
 *
 * @author Andy Wilkinson
 */
@Conditional(MultipleNonPrimaryMeterRegistriesCondition.class)
class CompositeMeterRegistryConfiguration {

	@Bean
	@Primary
	public CompositeMeterRegistry compositeMeterRegistry(Clock clock,
			List<MeterRegistry> registries) {
		return new CompositeMeterRegistry(clock, registries);
	}

	static class MultipleNonPrimaryMeterRegistriesCondition extends NoneNestedConditions {

		MultipleNonPrimaryMeterRegistriesCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnMissingBean(MeterRegistry.class)
		static class NoMeterRegistryCondition {

		}

		@ConditionalOnSingleCandidate(MeterRegistry.class)
		static class SingleInjectableMeterRegistry {

		}

	}

}
