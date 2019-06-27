

package org.springframework.boot.actuate.redis;

import java.util.Properties;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.util.Assert;

/**
 * Simple implementation of a {@link HealthIndicator} returning status information for
 * Redis data stores.
 *
 * @author Christian Dupuis
 * @author Richard Santana
 * @since 2.0.0
 */
public class RedisHealthIndicator extends AbstractHealthIndicator {

	static final String VERSION = "version";

	static final String REDIS_VERSION = "redis_version";

	private final RedisConnectionFactory redisConnectionFactory;

	public RedisHealthIndicator(RedisConnectionFactory connectionFactory) {
		super("Redis health check failed");
		Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
		this.redisConnectionFactory = connectionFactory;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		RedisConnection connection = RedisConnectionUtils
				.getConnection(this.redisConnectionFactory);
		try {
			if (connection instanceof RedisClusterConnection) {
				ClusterInfo clusterInfo = ((RedisClusterConnection) connection)
						.clusterGetClusterInfo();
				builder.up().withDetail("cluster_size", clusterInfo.getClusterSize())
						.withDetail("slots_up", clusterInfo.getSlotsOk())
						.withDetail("slots_fail", clusterInfo.getSlotsFail());
			}
			else {
				Properties info = connection.info();
				builder.up().withDetail(VERSION, info.getProperty(REDIS_VERSION));
			}
		}
		finally {
			RedisConnectionUtils.releaseConnection(connection,
					this.redisConnectionFactory);
		}
	}

}
