

package org.springframework.boot.actuate.redis;

import java.util.Properties;

import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

/**
 * A {@link ReactiveHealthIndicator} for Redis.
 *
 * @author Stephane Nicoll
 * @author Mark Paluch
 * @since 2.0.0
 */
public class RedisReactiveHealthIndicator extends AbstractReactiveHealthIndicator {

	private final ReactiveRedisConnectionFactory connectionFactory;

	public RedisReactiveHealthIndicator(
			ReactiveRedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	protected Mono<Health> doHealthCheck(Health.Builder builder) {
		ReactiveRedisConnection connection = this.connectionFactory
				.getReactiveConnection();
		return connection.serverCommands().info().map((info) -> up(builder, info))
				.doFinally((signal) -> connection.close());
	}

	private Health up(Health.Builder builder, Properties info) {
		return builder.up().withDetail(RedisHealthIndicator.VERSION,
				info.getProperty(RedisHealthIndicator.REDIS_VERSION)).build();
	}

}
