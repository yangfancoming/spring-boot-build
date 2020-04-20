

package org.springframework.boot.actuate.autoconfigure.metrics.export.newrelic;

import io.micrometer.core.instrument.Clock;
import io.micrometer.newrelic.NewRelicConfig;
import io.micrometer.newrelic.NewRelicMeterRegistry;

import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for exporting metrics to New Relic.
 *
 * @author Jon Schneider
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Configuration
@AutoConfigureBefore({ CompositeMeterRegistryAutoConfiguration.class,
		SimpleMetricsExportAutoConfiguration.class })
@AutoConfigureAfter(MetricsAutoConfiguration.class)
@ConditionalOnBean(Clock.class)
@ConditionalOnClass(NewRelicMeterRegistry.class)
@ConditionalOnProperty(prefix = "management.metrics.export.newrelic", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(NewRelicProperties.class)
public class NewRelicMetricsExportAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public NewRelicConfig newRelicConfig(NewRelicProperties props) {
		return new NewRelicPropertiesConfigAdapter(props);
	}

	@Bean
	@ConditionalOnMissingBean
	public NewRelicMeterRegistry newRelicMeterRegistry(NewRelicConfig newRelicConfig,
			Clock clock) {
		return new NewRelicMeterRegistry(newRelicConfig, clock);
	}

}
