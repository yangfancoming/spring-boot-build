

package org.springframework.boot.actuate.endpoint.jmx.annotation;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.annotation.AbstractDiscoveredEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.jmx.ExposableJmxEndpoint;
import org.springframework.boot.actuate.endpoint.jmx.JmxOperation;

/**
 * A discovered {@link ExposableJmxEndpoint JMX endpoint}.
 *
 * @author Phillip Webb
 */
class DiscoveredJmxEndpoint extends AbstractDiscoveredEndpoint<JmxOperation>
		implements ExposableJmxEndpoint {

	DiscoveredJmxEndpoint(EndpointDiscoverer<?, ?> discoverer, Object endpointBean,
			String id, boolean enabledByDefault, Collection<JmxOperation> operations) {
		super(discoverer, endpointBean, id, enabledByDefault, operations);
	}

}
