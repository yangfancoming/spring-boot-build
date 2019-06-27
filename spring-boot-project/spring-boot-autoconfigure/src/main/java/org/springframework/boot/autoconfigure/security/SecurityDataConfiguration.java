

package org.springframework.boot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Automatically adds Spring Security's integration with Spring Data.
 *
 * @author Rob Winch
 * @since 1.3
 */
@Configuration
@ConditionalOnClass({ SecurityEvaluationContextExtension.class,
		EvaluationContextExtensionSupport.class })
public class SecurityDataConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}

}
