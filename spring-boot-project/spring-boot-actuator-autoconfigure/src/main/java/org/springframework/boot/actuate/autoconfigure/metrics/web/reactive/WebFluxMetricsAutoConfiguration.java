

package org.springframework.boot.actuate.autoconfigure.metrics.web.reactive;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.metrics.web.reactive.server.DefaultWebFluxTagsProvider;
import org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for instrumentation of Spring
 * WebFlux MVC annotation-based programming model request mappings.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
@Configuration
@AutoConfigureAfter({ MetricsAutoConfiguration.class,
		SimpleMetricsExportAutoConfiguration.class })
@ConditionalOnBean(MeterRegistry.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxMetricsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WebFluxTagsProvider.class)
	public DefaultWebFluxTagsProvider webfluxTagConfigurer() {
		return new DefaultWebFluxTagsProvider();
	}

	@Bean
	public MetricsWebFilter webfluxMetrics(MeterRegistry registry,
			WebFluxTagsProvider tagConfigurer, MetricsProperties properties) {
		return new MetricsWebFilter(registry, tagConfigurer,
				properties.getWeb().getServer().getRequestsMetricName());
	}

}
