

package org.springframework.boot.actuate.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * {@link HealthIndicator} for a JMS {@link ConnectionFactory}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class JmsHealthIndicator extends AbstractHealthIndicator {

	private final ConnectionFactory connectionFactory;

	public JmsHealthIndicator(ConnectionFactory connectionFactory) {
		super("JMS health check failed");
		this.connectionFactory = connectionFactory;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		try (Connection connection = this.connectionFactory.createConnection()) {
			connection.start();
			builder.up().withDetail("provider",
					connection.getMetaData().getJMSProviderName());
		}
	}

}
