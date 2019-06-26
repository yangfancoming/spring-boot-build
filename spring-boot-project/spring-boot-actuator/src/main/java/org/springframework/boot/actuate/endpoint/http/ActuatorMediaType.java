

package org.springframework.boot.actuate.endpoint.http;

/**
 * Media types that can be consumed and produced by Actuator endpoints.
 *
 * @author Andy Wilkinson
 * @author Madhura Bhave
 * @since 2.0.0
 */
public final class ActuatorMediaType {

	/**
	 * Constant for the Actuator V1 media type.
	 */
	public static final String V1_JSON = "application/vnd.spring-boot.actuator.v1+json";

	/**
	 * Constant for the Actuator V2 media type.
	 */
	public static final String V2_JSON = "application/vnd.spring-boot.actuator.v2+json";

	private ActuatorMediaType() {
	}

}
