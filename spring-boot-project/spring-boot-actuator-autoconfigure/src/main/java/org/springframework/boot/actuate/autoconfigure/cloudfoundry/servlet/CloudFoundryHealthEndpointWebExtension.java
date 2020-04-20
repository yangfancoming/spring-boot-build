

package org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.HealthEndpointCloudFoundryExtension;
import org.springframework.boot.actuate.endpoint.annotation.EndpointExtension;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointWebExtension;
import org.springframework.boot.actuate.health.ShowDetails;

/**
 * {@link EndpointExtension} for the {@link HealthEndpoint} that always exposes full
 * health details.
 *
 * @author Madhura Bhave
 * @since 2.0.0
 */
@HealthEndpointCloudFoundryExtension
public class CloudFoundryHealthEndpointWebExtension {

	private final HealthEndpointWebExtension delegate;

	public CloudFoundryHealthEndpointWebExtension(HealthEndpointWebExtension delegate) {
		this.delegate = delegate;
	}

	@ReadOperation
	public WebEndpointResponse<Health> getHealth() {
		return this.delegate.getHealth(null, ShowDetails.ALWAYS);
	}

}
