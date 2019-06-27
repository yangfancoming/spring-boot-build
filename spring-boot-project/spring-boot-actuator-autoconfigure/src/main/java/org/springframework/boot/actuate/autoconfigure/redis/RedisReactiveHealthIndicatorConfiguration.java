

package org.springframework.boot.actuate.autoconfigure.redis;

import java.util.Map;

import reactor.core.publisher.Flux;

import org.springframework.boot.actuate.autoconfigure.health.CompositeReactiveHealthIndicatorConfiguration;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.redis.RedisReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

/**
 * Configuration for {@link RedisReactiveHealthIndicator}.
 *
 * @author Christian Dupuis
 * @author Richard Santana
 * @author Stephane Nicoll
 * @author Mark Paluch
 */
@Configuration
@ConditionalOnClass({ ReactiveRedisConnectionFactory.class, Flux.class })
@ConditionalOnBean(ReactiveRedisConnectionFactory.class)
class RedisReactiveHealthIndicatorConfiguration extends
		CompositeReactiveHealthIndicatorConfiguration<RedisReactiveHealthIndicator, ReactiveRedisConnectionFactory> {

	private final Map<String, ReactiveRedisConnectionFactory> redisConnectionFactories;

	RedisReactiveHealthIndicatorConfiguration(
			Map<String, ReactiveRedisConnectionFactory> redisConnectionFactories) {
		this.redisConnectionFactories = redisConnectionFactories;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisHealthIndicator")
	public ReactiveHealthIndicator redisHealthIndicator() {
		return createHealthIndicator(this.redisConnectionFactories);
	}

}
