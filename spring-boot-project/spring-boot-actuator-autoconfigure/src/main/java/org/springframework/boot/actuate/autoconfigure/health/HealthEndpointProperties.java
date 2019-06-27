

package org.springframework.boot.actuate.autoconfigure.health;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.ShowDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for {@link HealthEndpoint}.
 *
 * @author Phillip Webb
 */
@ConfigurationProperties("management.endpoint.health")
public class HealthEndpointProperties {

	/**
	 * When to show full health details.
	 */
	private ShowDetails showDetails = ShowDetails.NEVER;

	/**
	 * Roles used to determine whether or not a user is authorized to be shown details.
	 * When empty, all authenticated users are authorized.
	 */
	private Set<String> roles = new HashSet<>();

	public ShowDetails getShowDetails() {
		return this.showDetails;
	}

	public void setShowDetails(ShowDetails showDetails) {
		this.showDetails = showDetails;
	}

	public Set<String> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
