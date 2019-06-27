

package org.springframework.boot.actuate.autoconfigure.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Configuration for a no-op meter registry when the context does not contain an
 * auto-configured {@link MeterRegistry}.
 *
 * @author Andy Wilkinson
 */
@ConditionalOnBean(Clock.class)
@ConditionalOnMissingBean(MeterRegistry.class)
class NoOpMeterRegistryConfiguration {

	@Bean
	public CompositeMeterRegistry noOpMeterRegistry(Clock clock) {
		return new CompositeMeterRegistry(clock);
	}

}
