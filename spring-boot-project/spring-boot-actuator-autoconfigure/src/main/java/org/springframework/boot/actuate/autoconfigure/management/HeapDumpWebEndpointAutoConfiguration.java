

package org.springframework.boot.actuate.autoconfigure.management;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.management.HeapDumpWebEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link HeapDumpWebEndpoint}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Configuration
public class HeapDumpWebEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public HeapDumpWebEndpoint heapDumpWebEndpoint() {
		return new HeapDumpWebEndpoint();
	}

}
