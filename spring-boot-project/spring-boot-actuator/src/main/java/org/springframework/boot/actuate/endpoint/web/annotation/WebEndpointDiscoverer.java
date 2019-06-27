

package org.springframework.boot.actuate.endpoint.web.annotation;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.annotation.DiscoveredOperationMethod;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvokerAdvisor;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.WebOperation;
import org.springframework.boot.actuate.endpoint.web.WebOperationRequestPredicate;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * {@link EndpointDiscoverer} for {@link ExposableWebEndpoint web endpoints}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class WebEndpointDiscoverer
		extends EndpointDiscoverer<ExposableWebEndpoint, WebOperation>
		implements WebEndpointsSupplier {

	private final PathMapper endpointPathMapper;

	private final RequestPredicateFactory requestPredicateFactory;

	/**
	 * Create a new {@link WebEndpointDiscoverer} instance.
	 * @param applicationContext the source application context
	 * @param parameterValueMapper the parameter value mapper
	 * @param endpointMediaTypes the endpoint media types
	 * @param endpointPathMapper the endpoint path mapper
	 * @param invokerAdvisors invoker advisors to apply
	 * @param filters filters to apply
	 */
	public WebEndpointDiscoverer(ApplicationContext applicationContext,
			ParameterValueMapper parameterValueMapper,
			EndpointMediaTypes endpointMediaTypes, PathMapper endpointPathMapper,
			Collection<OperationInvokerAdvisor> invokerAdvisors,
			Collection<EndpointFilter<ExposableWebEndpoint>> filters) {
		super(applicationContext, parameterValueMapper, invokerAdvisors, filters);
		Assert.notNull(endpointPathMapper, "EndpointPathMapper must not be null");
		this.endpointPathMapper = endpointPathMapper;
		this.requestPredicateFactory = new RequestPredicateFactory(endpointMediaTypes);
	}

	@Override
	protected ExposableWebEndpoint createEndpoint(Object endpointBean, String id,
			boolean enabledByDefault, Collection<WebOperation> operations) {
		String rootPath = this.endpointPathMapper.getRootPath(id);
		return new DiscoveredWebEndpoint(this, endpointBean, id, rootPath,
				enabledByDefault, operations);
	}

	@Override
	protected WebOperation createOperation(String endpointId,
			DiscoveredOperationMethod operationMethod, OperationInvoker invoker) {
		String rootPath = this.endpointPathMapper.getRootPath(endpointId);
		WebOperationRequestPredicate requestPredicate = this.requestPredicateFactory
				.getRequestPredicate(endpointId, rootPath, operationMethod);
		return new DiscoveredWebOperation(endpointId, operationMethod, invoker,
				requestPredicate);
	}

	@Override
	protected OperationKey createOperationKey(WebOperation operation) {
		return new OperationKey(operation.getRequestPredicate(),
				() -> "web request predicate " + operation.getRequestPredicate());
	}

}
