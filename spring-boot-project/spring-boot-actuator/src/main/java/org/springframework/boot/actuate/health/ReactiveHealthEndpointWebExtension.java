

package org.springframework.boot.actuate.health;

import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;

/**
 * Reactive {@link EndpointWebExtension} for the {@link HealthEndpoint}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@EndpointWebExtension(endpoint = HealthEndpoint.class)
public class ReactiveHealthEndpointWebExtension {

	private final ReactiveHealthIndicator delegate;

	private final HealthWebEndpointResponseMapper responseMapper;

	public ReactiveHealthEndpointWebExtension(ReactiveHealthIndicator delegate,
			HealthWebEndpointResponseMapper responseMapper) {
		this.delegate = delegate;
		this.responseMapper = responseMapper;
	}

	@ReadOperation
	public Mono<WebEndpointResponse<Health>> health(SecurityContext securityContext) {
		return this.delegate.health()
				.map((health) -> this.responseMapper.map(health, securityContext));
	}

	public Mono<WebEndpointResponse<Health>> health(SecurityContext securityContext,
			ShowDetails showDetails) {
		return this.delegate.health().map((health) -> this.responseMapper.map(health,
				securityContext, showDetails));
	}

}
