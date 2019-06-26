

package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PoolingOptions;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CassandraAutoConfiguration}
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
public class CassandraAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(CassandraAutoConfiguration.class));

	@Test
	public void createClusterWithDefault() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(Cluster.class);
			assertThat(context.getBean(Cluster.class).getClusterName())
					.startsWith("cluster");
		});
	}

	@Test
	public void createClusterWithOverrides() {
		this.contextRunner
				.withPropertyValues("spring.data.cassandra.cluster-name=testcluster")
				.run((context) -> {
					assertThat(context).hasSingleBean(Cluster.class);
					assertThat(context.getBean(Cluster.class).getClusterName())
							.isEqualTo("testcluster");
				});
	}

	@Test
	public void createCustomizeCluster() {
		this.contextRunner.withUserConfiguration(MockCustomizerConfig.class)
				.run((context) -> {
					assertThat(context).hasSingleBean(Cluster.class);
					assertThat(context).hasSingleBean(ClusterBuilderCustomizer.class);
				});
	}

	@Test
	public void customizerOverridesAutoConfig() {
		this.contextRunner.withUserConfiguration(SimpleCustomizerConfig.class)
				.withPropertyValues("spring.data.cassandra.cluster-name=testcluster")
				.run((context) -> {
					assertThat(context).hasSingleBean(Cluster.class);
					assertThat(context.getBean(Cluster.class).getClusterName())
							.isEqualTo("overridden-name");
				});
	}

	@Test
	public void defaultPoolOptions() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(Cluster.class);
			PoolingOptions poolingOptions = context.getBean(Cluster.class)
					.getConfiguration().getPoolingOptions();
			assertThat(poolingOptions.getIdleTimeoutSeconds())
					.isEqualTo(PoolingOptions.DEFAULT_IDLE_TIMEOUT_SECONDS);
			assertThat(poolingOptions.getPoolTimeoutMillis())
					.isEqualTo(PoolingOptions.DEFAULT_POOL_TIMEOUT_MILLIS);
			assertThat(poolingOptions.getHeartbeatIntervalSeconds())
					.isEqualTo(PoolingOptions.DEFAULT_HEARTBEAT_INTERVAL_SECONDS);
			assertThat(poolingOptions.getMaxQueueSize())
					.isEqualTo(PoolingOptions.DEFAULT_MAX_QUEUE_SIZE);
		});
	}

	@Test
	public void customizePoolOptions() {
		this.contextRunner
				.withPropertyValues("spring.data.cassandra.pool.idle-timeout=42",
						"spring.data.cassandra.pool.pool-timeout=52",
						"spring.data.cassandra.pool.heartbeat-interval=62",
						"spring.data.cassandra.pool.max-queue-size=72")
				.run((context) -> {
					assertThat(context).hasSingleBean(Cluster.class);
					PoolingOptions poolingOptions = context.getBean(Cluster.class)
							.getConfiguration().getPoolingOptions();
					assertThat(poolingOptions.getIdleTimeoutSeconds()).isEqualTo(42);
					assertThat(poolingOptions.getPoolTimeoutMillis()).isEqualTo(52);
					assertThat(poolingOptions.getHeartbeatIntervalSeconds())
							.isEqualTo(62);
					assertThat(poolingOptions.getMaxQueueSize()).isEqualTo(72);
				});
	}

	@Configuration
	static class MockCustomizerConfig {

		@Bean
		public ClusterBuilderCustomizer customizer() {
			return mock(ClusterBuilderCustomizer.class);
		}

	}

	@Configuration
	static class SimpleCustomizerConfig {

		@Bean
		public ClusterBuilderCustomizer customizer() {
			return (clusterBuilder) -> clusterBuilder.withClusterName("overridden-name");
		}

	}

}
