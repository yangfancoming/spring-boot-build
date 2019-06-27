

package org.springframework.boot.actuate.autoconfigure.flyway;

import org.flywaydb.core.Flyway;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.flyway.FlywayEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link FlywayEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(Flyway.class)
@AutoConfigureAfter(FlywayAutoConfiguration.class)
public class FlywayEndpointAutoConfiguration {

	@Bean
	@ConditionalOnBean(Flyway.class)
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public FlywayEndpoint flywayEndpoint(ApplicationContext context) {
		return new FlywayEndpoint(context);
	}

}
