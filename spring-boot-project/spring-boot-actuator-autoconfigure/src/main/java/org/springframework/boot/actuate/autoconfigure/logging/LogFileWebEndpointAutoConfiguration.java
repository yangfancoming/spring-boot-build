

package org.springframework.boot.actuate.autoconfigure.logging;

import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link LogFileWebEndpoint}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Configuration
@EnableConfigurationProperties(LogFileWebEndpointProperties.class)
public class LogFileWebEndpointAutoConfiguration {

	private final LogFileWebEndpointProperties properties;

	public LogFileWebEndpointAutoConfiguration(LogFileWebEndpointProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	@Conditional(LogFileCondition.class)
	public LogFileWebEndpoint logFileWebEndpoint(Environment environment) {
		return new LogFileWebEndpoint(environment, this.properties.getExternalFile());
	}

	private static class LogFileCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			Environment environment = context.getEnvironment();
			String config = environment.resolvePlaceholders("${logging.file:}");
			ConditionMessage.Builder message = ConditionMessage.forCondition("Log File");
			if (StringUtils.hasText(config)) {
				return ConditionOutcome
						.match(message.found("logging.file").items(config));
			}
			config = environment.resolvePlaceholders("${logging.path:}");
			if (StringUtils.hasText(config)) {
				return ConditionOutcome
						.match(message.found("logging.path").items(config));
			}
			config = environment.getProperty("management.endpoint.logfile.external-file");
			if (StringUtils.hasText(config)) {
				return ConditionOutcome
						.match(message.found("management.endpoint.logfile.external-file")
								.items(config));
			}
			return ConditionOutcome.noMatch(message.didNotFind("logging file").atAll());
		}

	}

}
