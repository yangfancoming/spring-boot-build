

package org.springframework.boot.actuate.autoconfigure.liquibase;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.liquibase.LiquibaseEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link LiquibaseEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(SpringLiquibase.class)
@AutoConfigureAfter(LiquibaseAutoConfiguration.class)
public class LiquibaseEndpointAutoConfiguration {

	@Bean
	@ConditionalOnBean(SpringLiquibase.class)
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public LiquibaseEndpoint liquibaseEndpoint(ApplicationContext context) {
		return new LiquibaseEndpoint(context);
	}

}
