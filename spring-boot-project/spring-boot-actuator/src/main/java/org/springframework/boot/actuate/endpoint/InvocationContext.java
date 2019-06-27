

package org.springframework.boot.actuate.endpoint;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.util.Assert;

/**
 * The context for the {@link OperationInvoker invocation of an operation}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class InvocationContext {

	private final SecurityContext securityContext;

	private final Map<String, Object> arguments;

	/**
	 * Creates a new context for an operation being invoked by the given {@code principal}
	 * with the given available {@code arguments}.
	 * @param securityContext the current security context. Never {@code null}
	 * @param arguments the arguments available to the operation. Never {@code null}
	 */
	public InvocationContext(SecurityContext securityContext,
			Map<String, Object> arguments) {
		Assert.notNull(securityContext, "SecurityContext must not be null");
		Assert.notNull(arguments, "Arguments must not be null");
		this.securityContext = securityContext;
		this.arguments = arguments;
	}

	public SecurityContext getSecurityContext() {
		return this.securityContext;
	}

	public Map<String, Object> getArguments() {
		return this.arguments;
	}

}
