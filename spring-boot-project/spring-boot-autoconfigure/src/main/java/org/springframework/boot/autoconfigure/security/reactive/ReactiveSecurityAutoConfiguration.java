

package org.springframework.boot.autoconfigure.security.reactive;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Security in a reactive
 * application.
 *
 * @author Madhura Bhave
 * @since 2.0.0
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@Import(WebFluxSecurityConfiguration.class)
public class ReactiveSecurityAutoConfiguration {

}
