

package org.springframework.boot.actuate.elasticsearch;

import java.util.List;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * {@link HealthIndicator} for an Elasticsearch cluster.
 *
 * @author Binwei Yang
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class ElasticsearchHealthIndicator extends AbstractHealthIndicator {

	private static final String[] ALL_INDICES = { "_all" };

	private final Client client;

	private final String[] indices;

	private final long responseTimeout;

	/**
	 * Create a new {@link ElasticsearchHealthIndicator} instance.
	 * @param client the Elasticsearch client
	 * @param responseTimeout the request timeout in milliseconds
	 * @param indices the indices to check
	 */
	public ElasticsearchHealthIndicator(Client client, long responseTimeout,
			List<String> indices) {
		this(client, responseTimeout,
				(indices != null) ? StringUtils.toStringArray(indices) : null);
	}

	/**
	 * Create a new {@link ElasticsearchHealthIndicator} instance.
	 * @param client the Elasticsearch client
	 * @param responseTimeout the request timeout in milliseconds
	 * @param indices the indices to check
	 */
	public ElasticsearchHealthIndicator(Client client, long responseTimeout,
			String... indices) {
		super("Elasticsearch health check failed");
		this.client = client;
		this.responseTimeout = responseTimeout;
		this.indices = indices;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		ClusterHealthRequest request = Requests.clusterHealthRequest(
				ObjectUtils.isEmpty(this.indices) ? ALL_INDICES : this.indices);
		ClusterHealthResponse response = this.client.admin().cluster().health(request)
				.actionGet(this.responseTimeout);
		switch (response.getStatus()) {
		case GREEN:
		case YELLOW:
			builder.up();
			break;
		case RED:
		default:
			builder.down();
			break;
		}
		builder.withDetail("clusterName", response.getClusterName());
		builder.withDetail("numberOfNodes", response.getNumberOfNodes());
		builder.withDetail("numberOfDataNodes", response.getNumberOfDataNodes());
		builder.withDetail("activePrimaryShards", response.getActivePrimaryShards());
		builder.withDetail("activeShards", response.getActiveShards());
		builder.withDetail("relocatingShards", response.getRelocatingShards());
		builder.withDetail("initializingShards", response.getInitializingShards());
		builder.withDetail("unassignedShards", response.getUnassignedShards());
	}

}
