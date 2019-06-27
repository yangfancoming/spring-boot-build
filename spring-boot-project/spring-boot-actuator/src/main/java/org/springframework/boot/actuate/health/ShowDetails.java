

package org.springframework.boot.actuate.health;

/**
 * Options for showing details in responses from the {@link HealthEndpoint} web
 * extensions.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public enum ShowDetails {

	/**
	 * Never show details in the response.
	 */
	NEVER,

	/**
	 * Show details in the response when accessed by an authorized user.
	 */
	WHEN_AUTHORIZED,

	/**
	 * Always show details in the response.
	 */
	ALWAYS

}
