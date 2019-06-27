

package org.springframework.boot.actuate.autoconfigure.jolokia;

import org.jolokia.http.AgentServlet;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link JolokiaEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(AgentServlet.class)
@EnableConfigurationProperties(JolokiaProperties.class)
public class JolokiaEndpointAutoConfiguration {

	@Bean
	@ConditionalOnEnabledEndpoint
	public JolokiaEndpoint jolokiaEndpoint(JolokiaProperties properties) {
		return new JolokiaEndpoint(properties.getConfig());
	}

}
