

package org.springframework.boot.actuate.endpoint.web.annotation;

import java.util.Collection;
import java.util.Collections;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.Operation;
import org.springframework.boot.actuate.endpoint.annotation.DiscoveredOperationMethod;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link EndpointDiscoverer} for {@link ExposableControllerEndpoint controller
 * endpoints}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class ControllerEndpointDiscoverer
		extends EndpointDiscoverer<ExposableControllerEndpoint, Operation>
		implements ControllerEndpointsSupplier {

	private final PathMapper endpointPathMapper;

	/**
	 * Create a new {@link ControllerEndpointDiscoverer} instance.
	 * @param applicationContext the source application context
	 * @param endpointPathMapper the endpoint path mapper
	 * @param filters filters to apply
	 */
	public ControllerEndpointDiscoverer(ApplicationContext applicationContext,
			PathMapper endpointPathMapper,
			Collection<EndpointFilter<ExposableControllerEndpoint>> filters) {
		super(applicationContext, ParameterValueMapper.NONE, Collections.emptyList(),
				filters);
		Assert.notNull(endpointPathMapper, "EndpointPathMapper must not be null");
		this.endpointPathMapper = endpointPathMapper;
	}

	@Override
	protected boolean isEndpointExposed(Object endpointBean) {
		Class<?> type = ClassUtils.getUserClass(endpointBean.getClass());
		return AnnotatedElementUtils.isAnnotated(type, ControllerEndpoint.class)
				|| AnnotatedElementUtils.isAnnotated(type, RestControllerEndpoint.class);
	}

	@Override
	protected ExposableControllerEndpoint createEndpoint(Object endpointBean, String id,
			boolean enabledByDefault, Collection<Operation> operations) {
		String rootPath = this.endpointPathMapper.getRootPath(id);
		return new DiscoveredControllerEndpoint(this, endpointBean, id, rootPath,
				enabledByDefault);
	}

	@Override
	protected Operation createOperation(String endpointId,
			DiscoveredOperationMethod operationMethod, OperationInvoker invoker) {
		throw new IllegalStateException(
				"ControllerEndpoints must not declare operations");
	}

	@Override
	protected OperationKey createOperationKey(Operation operation) {
		throw new IllegalStateException(
				"ControllerEndpoints must not declare operations");
	}

}
