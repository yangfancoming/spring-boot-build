

package org.springframework.boot.actuate.autoconfigure.amqp;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.amqp.RabbitHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link RabbitHealthIndicator}.
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
@ConditionalOnBean(RabbitTemplate.class)
@ConditionalOnEnabledHealthIndicator("rabbit")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class RabbitHealthIndicatorAutoConfiguration extends CompositeHealthIndicatorConfiguration<RabbitHealthIndicator, RabbitTemplate> {

	private final Map<String, RabbitTemplate> rabbitTemplates;

	public RabbitHealthIndicatorAutoConfiguration(
			Map<String, RabbitTemplate> rabbitTemplates) {
		this.rabbitTemplates = rabbitTemplates;
	}

	@Bean
	@ConditionalOnMissingBean(name = "rabbitHealthIndicator")
	public HealthIndicator rabbitHealthIndicator() {
		return createHealthIndicator(this.rabbitTemplates);
	}

}
