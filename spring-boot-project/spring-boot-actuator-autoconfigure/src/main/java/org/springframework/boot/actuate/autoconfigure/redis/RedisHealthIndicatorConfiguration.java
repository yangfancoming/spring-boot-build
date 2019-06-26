

package org.springframework.boot.actuate.autoconfigure.redis;

import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Configuration for {@link RedisHealthIndicator}.
 *
 * @author Christian Dupuis
 * @author Richard Santana
 * @author Stephane Nicoll
 * @author Mark Paluch
 */
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnBean(RedisConnectionFactory.class)
class RedisHealthIndicatorConfiguration extends
		CompositeHealthIndicatorConfiguration<RedisHealthIndicator, RedisConnectionFactory> {

	private final Map<String, RedisConnectionFactory> redisConnectionFactories;

	RedisHealthIndicatorConfiguration(
			Map<String, RedisConnectionFactory> redisConnectionFactories) {
		this.redisConnectionFactories = redisConnectionFactories;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisHealthIndicator")
	public HealthIndicator redisHealthIndicator() {
		return createHealthIndicator(this.redisConnectionFactories);
	}

}
