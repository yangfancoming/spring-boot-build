

package org.springframework.boot.actuate.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;

import org.junit.Test;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link JmsHealthIndicator}.
 *
 * @author Stephane Nicoll
 */
public class JmsHealthIndicatorTests {

	@Test
	public void jmsBrokerIsUp() throws JMSException {
		ConnectionMetaData connectionMetaData = mock(ConnectionMetaData.class);
		given(connectionMetaData.getJMSProviderName()).willReturn("JMS test provider");
		Connection connection = mock(Connection.class);
		given(connection.getMetaData()).willReturn(connectionMetaData);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		given(connectionFactory.createConnection()).willReturn(connection);
		JmsHealthIndicator indicator = new JmsHealthIndicator(connectionFactory);
		Health health = indicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails().get("provider")).isEqualTo("JMS test provider");
		verify(connection, times(1)).close();
	}

	@Test
	public void jmsBrokerIsDown() throws JMSException {
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		given(connectionFactory.createConnection())
				.willThrow(new JMSException("test", "123"));
		JmsHealthIndicator indicator = new JmsHealthIndicator(connectionFactory);
		Health health = indicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails().get("provider")).isNull();
	}

	@Test
	public void jmsBrokerCouldNotRetrieveProviderMetadata() throws JMSException {
		ConnectionMetaData connectionMetaData = mock(ConnectionMetaData.class);
		given(connectionMetaData.getJMSProviderName())
				.willThrow(new JMSException("test", "123"));
		Connection connection = mock(Connection.class);
		given(connection.getMetaData()).willReturn(connectionMetaData);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		given(connectionFactory.createConnection()).willReturn(connection);
		JmsHealthIndicator indicator = new JmsHealthIndicator(connectionFactory);
		Health health = indicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails().get("provider")).isNull();
		verify(connection, times(1)).close();
	}

	@Test
	public void jmsBrokerUsesFailover() throws JMSException {
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		ConnectionMetaData connectionMetaData = mock(ConnectionMetaData.class);
		given(connectionMetaData.getJMSProviderName()).willReturn("JMS test provider");
		Connection connection = mock(Connection.class);
		given(connection.getMetaData()).willReturn(connectionMetaData);
		willThrow(new JMSException("Could not start", "123")).given(connection).start();
		given(connectionFactory.createConnection()).willReturn(connection);
		JmsHealthIndicator indicator = new JmsHealthIndicator(connectionFactory);
		Health health = indicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails().get("provider")).isNull();
	}

}
