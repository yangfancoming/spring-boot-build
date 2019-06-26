

package org.springframework.boot.autoconfigure.jms.activemq;

import java.util.List;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for ActiveMQ {@link ConnectionFactory}.
 *
 * @author Greg Turnquist
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Aurélien Leboulanger
 * @since 1.1.0
 */
@Configuration
@ConditionalOnMissingBean(ConnectionFactory.class)
class ActiveMQConnectionFactoryConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "spring.activemq.pool", name = "enabled", havingValue = "false", matchIfMissing = true)
	public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties,
			ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers) {
		return new ActiveMQConnectionFactoryFactory(properties,
				factoryCustomizers.getIfAvailable())
						.createConnectionFactory(ActiveMQConnectionFactory.class);
	}

	@Configuration
	@ConditionalOnClass(PooledConnectionFactory.class)
	static class PooledConnectionFactoryConfiguration {

		@Bean(destroyMethod = "stop")
		@ConditionalOnProperty(prefix = "spring.activemq.pool", name = "enabled", havingValue = "true", matchIfMissing = false)
		public PooledConnectionFactory pooledJmsConnectionFactory(
				ActiveMQProperties properties,
				ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers) {
			PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(
					new ActiveMQConnectionFactoryFactory(properties,
							factoryCustomizers.getIfAvailable()).createConnectionFactory(
									ActiveMQConnectionFactory.class));
			ActiveMQProperties.Pool pool = properties.getPool();
			pooledConnectionFactory.setBlockIfSessionPoolIsFull(pool.isBlockIfFull());
			if (pool.getBlockIfFullTimeout() != null) {
				pooledConnectionFactory.setBlockIfSessionPoolIsFullTimeout(
						pool.getBlockIfFullTimeout().toMillis());
			}
			pooledConnectionFactory
					.setCreateConnectionOnStartup(pool.isCreateConnectionOnStartup());
			if (pool.getExpiryTimeout() != null) {
				pooledConnectionFactory
						.setExpiryTimeout(pool.getExpiryTimeout().toMillis());
			}
			if (pool.getIdleTimeout() != null) {
				pooledConnectionFactory
						.setIdleTimeout((int) pool.getIdleTimeout().toMillis());
			}
			pooledConnectionFactory.setMaxConnections(pool.getMaxConnections());
			pooledConnectionFactory.setMaximumActiveSessionPerConnection(
					pool.getMaximumActiveSessionPerConnection());
			pooledConnectionFactory
					.setReconnectOnException(pool.isReconnectOnException());
			if (pool.getTimeBetweenExpirationCheck() != null) {
				pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(
						pool.getTimeBetweenExpirationCheck().toMillis());
			}
			pooledConnectionFactory
					.setUseAnonymousProducers(pool.isUseAnonymousProducers());
			return pooledConnectionFactory;
		}

	}

}
