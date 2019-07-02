

package org.springframework.boot.test.autoconfigure.web.servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration for Spring Security's testing support.
 *
 * @author Andy Wilkinson
 * @since 1.4.0
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.test.mockmvc", name = "secure", havingValue = "true", matchIfMissing = true)
@Import({ SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class,
		MockMvcSecurityConfiguration.class })
public class MockMvcSecurityAutoConfiguration {

}
