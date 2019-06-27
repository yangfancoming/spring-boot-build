

package org.springframework.boot.jta.bitronix;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;

import org.springframework.boot.jms.XAConnectionFactoryWrapper;

/**
 * {@link XAConnectionFactoryWrapper} that uses a Bitronix
 * {@link PoolingConnectionFactoryBean} to wrap a {@link XAConnectionFactory}.
 *
 * @author Phillip Webb
 * @since 1.2.0
 */
public class BitronixXAConnectionFactoryWrapper implements XAConnectionFactoryWrapper {

	@Override
	public ConnectionFactory wrapConnectionFactory(
			XAConnectionFactory connectionFactory) {
		PoolingConnectionFactoryBean pool = new PoolingConnectionFactoryBean();
		pool.setConnectionFactory(connectionFactory);
		return pool;
	}

}
