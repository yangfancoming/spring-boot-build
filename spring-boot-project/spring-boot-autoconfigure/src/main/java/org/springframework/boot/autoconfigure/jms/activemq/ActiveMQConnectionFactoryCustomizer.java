

package org.springframework.boot.autoconfigure.jms.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Callback interface that can be implemented by beans wishing to customize the
 * {@link ActiveMQConnectionFactory} whilst retaining default auto-configuration.
 *
 * @author Stephane Nicoll
 * @since 1.5.5
 */
@FunctionalInterface
public interface ActiveMQConnectionFactoryCustomizer {

	/**
	 * Customize the {@link ActiveMQConnectionFactory}.
	 * @param factory the factory to customize
	 */
	void customize(ActiveMQConnectionFactory factory);

}
