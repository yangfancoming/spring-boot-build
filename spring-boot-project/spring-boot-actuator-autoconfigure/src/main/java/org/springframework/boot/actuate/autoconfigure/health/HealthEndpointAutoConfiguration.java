

package org.springframework.boot.actuate.autoconfigure.health;

import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link HealthEndpoint}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@EnableConfigurationProperties({ HealthEndpointProperties.class,
		HealthIndicatorProperties.class })
@AutoConfigureAfter(HealthIndicatorAutoConfiguration.class)
@Import({ HealthEndpointConfiguration.class,
		HealthEndpointWebExtensionConfiguration.class })
public class HealthEndpointAutoConfiguration {

}
