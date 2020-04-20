

package org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive;

import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.HealthEndpointCloudFoundryExtension;
import org.springframework.boot.actuate.endpoint.annotation.EndpointExtension;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.ReactiveHealthEndpointWebExtension;
import org.springframework.boot.actuate.health.ShowDetails;

/**
 * Reactive {@link EndpointExtension} for the {@link HealthEndpoint} that always exposes
 * full health details.
 *
 * @author Madhura Bhave
 * @since 2.0.0
 */
@HealthEndpointCloudFoundryExtension
public class CloudFoundryReactiveHealthEndpointWebExtension {

	private final ReactiveHealthEndpointWebExtension delegate;

	public CloudFoundryReactiveHealthEndpointWebExtension(
			ReactiveHealthEndpointWebExtension delegate) {
		this.delegate = delegate;
	}

	@ReadOperation
	public Mono<WebEndpointResponse<Health>> health() {
		return this.delegate.health(null, ShowDetails.ALWAYS);
	}

}
