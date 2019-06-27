

package org.springframework.boot.actuate.endpoint.web.annotation;

import java.util.Collections;

import org.springframework.boot.actuate.endpoint.Operation;
import org.springframework.boot.actuate.endpoint.annotation.AbstractDiscoveredEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;

/**
 * A discovered {@link ExposableControllerEndpoint controller endpoint}.
 *
 * @author Phillip Webb
 */
class DiscoveredControllerEndpoint extends AbstractDiscoveredEndpoint<Operation>
		implements ExposableControllerEndpoint {

	private final String rootPath;

	DiscoveredControllerEndpoint(EndpointDiscoverer<?, ?> discoverer, Object endpointBean,
			String id, String rootPath, boolean enabledByDefault) {
		super(discoverer, endpointBean, id, enabledByDefault, Collections.emptyList());
		this.rootPath = rootPath;
	}

	@Override
	public Object getController() {
		return getEndpointBean();
	}

	@Override
	public String getRootPath() {
		return this.rootPath;
	}

}
