

package org.springframework.boot.actuate.autoconfigure.system;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for
 * {@link DiskSpaceHealthIndicator}.
 *
 * @author Mattias Severson
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Configuration
@ConditionalOnEnabledHealthIndicator("diskspace")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
public class DiskSpaceHealthIndicatorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "diskSpaceHealthIndicator")
	public DiskSpaceHealthIndicator diskSpaceHealthIndicator(
			DiskSpaceHealthIndicatorProperties properties) {
		return new DiskSpaceHealthIndicator(properties.getPath(),
				properties.getThreshold());
	}

	@Bean
	public DiskSpaceHealthIndicatorProperties diskSpaceHealthIndicatorProperties() {
		return new DiskSpaceHealthIndicatorProperties();
	}

}
