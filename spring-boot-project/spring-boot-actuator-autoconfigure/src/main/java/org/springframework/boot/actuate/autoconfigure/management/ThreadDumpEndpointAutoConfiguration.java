

package org.springframework.boot.actuate.autoconfigure.management;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.management.ThreadDumpEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link ThreadDumpEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
public class ThreadDumpEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public ThreadDumpEndpoint dumpEndpoint() {
		return new ThreadDumpEndpoint();
	}

}
