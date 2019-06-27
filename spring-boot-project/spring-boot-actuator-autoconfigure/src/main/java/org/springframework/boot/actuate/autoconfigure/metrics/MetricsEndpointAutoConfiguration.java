

package org.springframework.boot.actuate.autoconfigure.metrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link MetricsEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(Timed.class)
@AutoConfigureAfter({ MetricsAutoConfiguration.class,
		CompositeMeterRegistryAutoConfiguration.class })
public class MetricsEndpointAutoConfiguration {

	@Bean
	@ConditionalOnBean(MeterRegistry.class)
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public MetricsEndpoint metricsEndpoint(MeterRegistry registry) {
		return new MetricsEndpoint(registry);
	}

}
