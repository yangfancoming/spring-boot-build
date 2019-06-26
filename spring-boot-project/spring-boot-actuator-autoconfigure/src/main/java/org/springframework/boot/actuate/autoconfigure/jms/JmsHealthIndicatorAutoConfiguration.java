

package org.springframework.boot.actuate.autoconfigure.jms;

import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jms.JmsHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link JmsHealthIndicator}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(ConnectionFactory.class)
@ConditionalOnBean(ConnectionFactory.class)
@ConditionalOnEnabledHealthIndicator("jms")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter({ ActiveMQAutoConfiguration.class, ArtemisAutoConfiguration.class })
public class JmsHealthIndicatorAutoConfiguration extends
		CompositeHealthIndicatorConfiguration<JmsHealthIndicator, ConnectionFactory> {

	private final Map<String, ConnectionFactory> connectionFactories;

	public JmsHealthIndicatorAutoConfiguration(
			ObjectProvider<Map<String, ConnectionFactory>> connectionFactories) {
		this.connectionFactories = connectionFactories.getIfAvailable();
	}

	@Bean
	@ConditionalOnMissingBean(name = "jmsHealthIndicator")
	public HealthIndicator jmsHealthIndicator() {
		return createHealthIndicator(this.connectionFactories);
	}

}
