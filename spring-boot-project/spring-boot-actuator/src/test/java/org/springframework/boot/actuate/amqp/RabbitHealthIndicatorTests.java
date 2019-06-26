

package org.springframework.boot.actuate.amqp;

import java.util.Collections;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link RabbitHealthIndicator}.
 *
 * @author Phillip Webb
 */
public class RabbitHealthIndicatorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private RabbitTemplate rabbitTemplate;

	@Mock
	private Channel channel;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		given(this.rabbitTemplate.execute(any())).willAnswer((invocation) -> {
			ChannelCallback<?> callback = invocation.getArgument(0);
			return callback.doInRabbit(this.channel);
		});
	}

	@Test
	public void createWhenRabbitTemplateIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("RabbitTemplate must not be null");
		new RabbitHealthIndicator(null);
	}

	@Test
	public void healthWhenConnectionSucceedsShouldReturnUpWithVersion() {
		Connection connection = mock(Connection.class);
		given(this.channel.getConnection()).willReturn(connection);
		given(connection.getServerProperties())
				.willReturn(Collections.singletonMap("version", "123"));
		Health health = new RabbitHealthIndicator(this.rabbitTemplate).health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsEntry("version", "123");
	}

	@Test
	public void healthWhenConnectionFailsShouldReturnDown() {
		given(this.channel.getConnection()).willThrow(new RuntimeException());
		Health health = new RabbitHealthIndicator(this.rabbitTemplate).health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
	}

}
