

package org.springframework.boot.actuate.endpoint;

import java.util.Collection;

/**
 * Information describing an endpoint that can be exposed in some technology specific way.
 *
 * @param <O> the type of the endpoint's operations
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface ExposableEndpoint<O extends Operation> {

	/**
	 * Returns the id of the endpoint.
	 * @return the id
	 */
	String getId();

	/**
	 * Returns if the endpoint is enabled by default.
	 * @return if the endpoint is enabled by default
	 */
	boolean isEnableByDefault();

	/**
	 * Returns the operations of the endpoint.
	 * @return the operations
	 */
	Collection<O> getOperations();

}
