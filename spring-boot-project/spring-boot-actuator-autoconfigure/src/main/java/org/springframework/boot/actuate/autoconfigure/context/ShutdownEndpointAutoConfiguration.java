

package org.springframework.boot.actuate.autoconfigure.context;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link ShutdownEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
public class ShutdownEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public ShutdownEndpoint shutdownEndpoint() {
		return new ShutdownEndpoint();
	}

}
