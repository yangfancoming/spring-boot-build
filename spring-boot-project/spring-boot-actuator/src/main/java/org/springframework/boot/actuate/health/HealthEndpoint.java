

package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.util.Assert;

/**
 * {@link Endpoint} to expose application health information.
 *
 * @author Dave Syer
 * @author Christian Dupuis
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Endpoint(id = "health")
public class HealthEndpoint {

	private final HealthIndicator healthIndicator;

	/**
	 * Create a new {@link HealthEndpoint} instance.
	 * @param healthIndicator the health indicator
	 */
	public HealthEndpoint(HealthIndicator healthIndicator) {
		Assert.notNull(healthIndicator, "HealthIndicator must not be null");
		this.healthIndicator = healthIndicator;
	}

	@ReadOperation
	public Health health() {
		return this.healthIndicator.health();
	}

}
