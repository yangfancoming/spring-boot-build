

package org.springframework.boot.actuate.endpoint.web;

/**
 * Strategy interface used to provide a mapping between an endpoint ID and the root path
 * where it will be exposed.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.0.0
 */
@FunctionalInterface
public interface PathMapper {

	/**
	 * Resolve the root path for the endpoint with the specified {@code endpointId}.
	 * @param endpointId the id of an endpoint
	 * @return the path of the endpoint
	 */
	String getRootPath(String endpointId);

	/**
	 * Returns an {@link PathMapper} that uses the endpoint ID as the path.
	 * @return an {@link PathMapper} that uses the endpoint ID as the path
	 */
	static PathMapper useEndpointId() {
		return (endpointId) -> endpointId;
	}

}
