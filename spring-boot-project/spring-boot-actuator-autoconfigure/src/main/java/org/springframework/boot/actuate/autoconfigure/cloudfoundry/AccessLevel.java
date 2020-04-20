

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import java.util.Arrays;
import java.util.List;

/**
 * The specific access level granted to the cloud foundry user that's calling the
 * endpoints.
 *
 * @author Madhura Bhave
 * @since 2.0.0
 */
public enum AccessLevel {

	/**
	 * Restricted access to a limited set of endpoints.
	 */
	RESTRICTED("", "health", "info"),

	/**
	 * Full access to all endpoints.
	 */
	FULL;

	public static final String REQUEST_ATTRIBUTE = "cloudFoundryAccessLevel";

	private final List<String> endpointIds;

	AccessLevel(String... endpointIds) {
		this.endpointIds = Arrays.asList(endpointIds);
	}

	/**
	 * Returns if the access level should allow access to the specified endpoint path.
	 * @param endpointId the endpoint ID to check
	 * @return {@code true} if access is allowed
	 */
	public boolean isAccessAllowed(String endpointId) {
		return this.endpointIds.isEmpty() || this.endpointIds.contains(endpointId);
	}

}
