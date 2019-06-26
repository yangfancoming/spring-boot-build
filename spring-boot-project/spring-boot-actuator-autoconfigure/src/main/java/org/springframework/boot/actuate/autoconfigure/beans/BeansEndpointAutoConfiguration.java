

package org.springframework.boot.actuate.autoconfigure.beans;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link BeansEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
public class BeansEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public BeansEndpoint beansEndpoint(
			ConfigurableApplicationContext applicationContext) {
		return new BeansEndpoint(applicationContext);
	}

}
