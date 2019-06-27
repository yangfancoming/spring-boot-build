

package org.springframework.boot.autoconfigure.jms.artemis;

import javax.jms.ConnectionFactory;
import javax.transaction.TransactionManager;

import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.XAConnectionFactoryWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for Artemis XA {@link ConnectionFactory}.
 *
 * @author Eddú Meléndez
 * @author Phillip Webb
 * @since 1.3.0
 */
@Configuration
@ConditionalOnMissingBean(ConnectionFactory.class)
@ConditionalOnClass(TransactionManager.class)
@ConditionalOnBean(XAConnectionFactoryWrapper.class)
class ArtemisXAConnectionFactoryConfiguration {

	@Primary
	@Bean(name = { "jmsConnectionFactory", "xaJmsConnectionFactory" })
	public ConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory,
			ArtemisProperties properties, XAConnectionFactoryWrapper wrapper)
			throws Exception {
		return wrapper.wrapConnectionFactory(
				new ArtemisConnectionFactoryFactory(beanFactory, properties)
						.createConnectionFactory(ActiveMQXAConnectionFactory.class));
	}

	@Bean
	public ActiveMQXAConnectionFactory nonXaJmsConnectionFactory(
			ListableBeanFactory beanFactory, ArtemisProperties properties) {
		return new ArtemisConnectionFactoryFactory(beanFactory, properties)
				.createConnectionFactory(ActiveMQXAConnectionFactory.class);
	}

}
