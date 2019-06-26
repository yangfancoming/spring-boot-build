

package org.springframework.boot.autoconfigure.jms.artemis;

import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Artemis {@link ConnectionFactory}.
 *
 * @author Eddú Meléndez
 * @author Phillip Webb
 */
@Configuration
@ConditionalOnMissingBean(ConnectionFactory.class)
class ArtemisConnectionFactoryConfiguration {

	@Bean
	public ActiveMQConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory,
			ArtemisProperties properties) {
		return new ArtemisConnectionFactoryFactory(beanFactory, properties)
				.createConnectionFactory(ActiveMQConnectionFactory.class);
	}

}
