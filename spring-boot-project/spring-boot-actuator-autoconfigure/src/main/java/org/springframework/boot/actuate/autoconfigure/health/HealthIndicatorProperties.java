

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for some health properties.
 *
 * @author Christian Dupuis
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "management.health.status")
public class HealthIndicatorProperties {

	/**
	 * Comma-separated list of health statuses in order of severity.
	 */
	private List<String> order = null;

	/**
	 * Mapping of health statuses to HTTP status codes. By default, registered health
	 * statuses map to sensible defaults (for example, UP maps to 200).
	 */
	private final Map<String, Integer> httpMapping = new HashMap<>();

	public List<String> getOrder() {
		return this.order;
	}

	public void setOrder(List<String> statusOrder) {
		if (statusOrder != null && !statusOrder.isEmpty()) {
			this.order = statusOrder;
		}
	}

	public Map<String, Integer> getHttpMapping() {
		return this.httpMapping;
	}

}
