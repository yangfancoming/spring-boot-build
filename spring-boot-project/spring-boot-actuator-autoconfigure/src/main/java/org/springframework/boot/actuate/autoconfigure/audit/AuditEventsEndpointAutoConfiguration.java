

package org.springframework.boot.actuate.autoconfigure.audit;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.AuditEventsEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link LoggersEndpoint}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Vedran Pavic
 * @since 2.0.0
 */
@Configuration
@AutoConfigureAfter(AuditAutoConfiguration.class)
public class AuditEventsEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(AuditEventRepository.class)
	@ConditionalOnEnabledEndpoint
	public AuditEventsEndpoint auditEventsEndpoint(
			AuditEventRepository auditEventRepository) {
		return new AuditEventsEndpoint(auditEventRepository);
	}

}
