

package org.springframework.boot.actuate.autoconfigure.context.properties;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.context.properties.ConfigurationPropertiesReportEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the
 * {@link ConfigurationPropertiesReportEndpoint}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesReportEndpointProperties.class)
public class ConfigurationPropertiesReportEndpointAutoConfiguration {

	private final ConfigurationPropertiesReportEndpointProperties properties;

	public ConfigurationPropertiesReportEndpointAutoConfiguration(
			ConfigurationPropertiesReportEndpointProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public ConfigurationPropertiesReportEndpoint configurationPropertiesReportEndpoint() {
		ConfigurationPropertiesReportEndpoint endpoint = new ConfigurationPropertiesReportEndpoint();
		String[] keysToSanitize = this.properties.getKeysToSanitize();
		if (keysToSanitize != null) {
			endpoint.setKeysToSanitize(keysToSanitize);
		}
		return endpoint;
	}

}
