

package org.springframework.boot.actuate.redis;

import java.util.Properties;

import io.lettuce.core.RedisConnectionException;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveServerCommands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link RedisReactiveHealthIndicator}.
 *
 * @author Stephane Nicoll
 * @author Mark Paluch
 * @author Nikolay Rybak
 */
public class RedisReactiveHealthIndicatorTests {

	@Test
	public void redisIsUp() {
		Properties info = new Properties();
		info.put("redis_version", "2.8.9");
		ReactiveRedisConnection redisConnection = mock(ReactiveRedisConnection.class);
		ReactiveServerCommands commands = mock(ReactiveServerCommands.class);
		given(commands.info()).willReturn(Mono.just(info));
		RedisReactiveHealthIndicator healthIndicator = createHealthIndicator(
				redisConnection, commands);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> {
			assertThat(h.getStatus()).isEqualTo(Status.UP);
			assertThat(h.getDetails()).containsOnlyKeys("version");
			assertThat(h.getDetails().get("version")).isEqualTo("2.8.9");
		}).verifyComplete();
		verify(redisConnection).close();
	}

	@Test
	public void redisCommandIsDown() {
		ReactiveServerCommands commands = mock(ReactiveServerCommands.class);
		given(commands.info()).willReturn(
				Mono.error(new RedisConnectionFailureException("Connection failed")));
		ReactiveRedisConnection redisConnection = mock(ReactiveRedisConnection.class);
		RedisReactiveHealthIndicator healthIndicator = createHealthIndicator(
				redisConnection, commands);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health)
				.consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.DOWN))
				.verifyComplete();
		verify(redisConnection).close();
	}

	@Test
	public void redisConnectionIsDown() {
		ReactiveRedisConnectionFactory redisConnectionFactory = mock(
				ReactiveRedisConnectionFactory.class);
		given(redisConnectionFactory.getReactiveConnection()).willThrow(
				new RedisConnectionException("Unable to connect to localhost:6379"));
		RedisReactiveHealthIndicator healthIndicator = new RedisReactiveHealthIndicator(
				redisConnectionFactory);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health)
				.consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.DOWN))
				.verifyComplete();
	}

	private RedisReactiveHealthIndicator createHealthIndicator(
			ReactiveRedisConnection redisConnection,
			ReactiveServerCommands serverCommands) {

		ReactiveRedisConnectionFactory redisConnectionFactory = mock(
				ReactiveRedisConnectionFactory.class);
		given(redisConnectionFactory.getReactiveConnection()).willReturn(redisConnection);
		given(redisConnection.serverCommands()).willReturn(serverCommands);
		return new RedisReactiveHealthIndicator(redisConnectionFactory);
	}

}
