

package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;

/**
 * {@link EndpointWebExtension} for the {@link HealthEndpoint}.
 *
 * @author Christian Dupuis
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @author Eddú Meléndez
 * @author Madhura Bhave
 * @since 2.0.0
 */
@EndpointWebExtension(endpoint = HealthEndpoint.class)
public class HealthEndpointWebExtension {

	private final HealthIndicator delegate;

	private final HealthWebEndpointResponseMapper responseMapper;

	public HealthEndpointWebExtension(HealthIndicator delegate,
			HealthWebEndpointResponseMapper responseMapper) {
		this.delegate = delegate;
		this.responseMapper = responseMapper;
	}

	@ReadOperation
	public WebEndpointResponse<Health> getHealth(SecurityContext securityContext) {
		return this.responseMapper.map(this.delegate.health(), securityContext);
	}

	public WebEndpointResponse<Health> getHealth(SecurityContext securityContext,
			ShowDetails showDetails) {
		return this.responseMapper.map(this.delegate.health(), securityContext,
				showDetails);
	}

}
