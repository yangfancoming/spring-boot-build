

package org.springframework.boot.actuate.autoconfigure.elasticsearch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.elasticsearch.ElasticsearchHealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * External configuration properties for {@link ElasticsearchHealthIndicator}.
 *
 * @author Binwei Yang
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "management.health.elasticsearch", ignoreUnknownFields = false)
public class ElasticsearchHealthIndicatorProperties {

	/**
	 * Comma-separated index names.
	 */
	private List<String> indices = new ArrayList<>();

	/**
	 * Time to wait for a response from the cluster.
	 */
	private Duration responseTimeout = Duration.ofMillis(100);

	public List<String> getIndices() {
		return this.indices;
	}

	public void setIndices(List<String> indices) {
		this.indices = indices;
	}

	public Duration getResponseTimeout() {
		return this.responseTimeout;
	}

	public void setResponseTimeout(Duration responseTimeout) {
		this.responseTimeout = responseTimeout;
	}

}
