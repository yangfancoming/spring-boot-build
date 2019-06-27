

package org.springframework.boot.actuate.endpoint.invoke;

import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;

/**
 * A {@code ParameterMappingException} is thrown when a failure occurs during
 * {@link ParameterValueMapper#mapParameterValue operation parameter mapping}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public final class ParameterMappingException extends InvalidEndpointRequestException {

	private final OperationParameter parameter;

	private final Object value;

	/**
	 * Creates a new {@code ParameterMappingException} for a failure that occurred when
	 * trying to map the given {@code input} to the given {@code type}.
	 * @param parameter the parameter being mapping
	 * @param value the value being mapped
	 * @param cause the cause of the mapping failure
	 */
	public ParameterMappingException(OperationParameter parameter, Object value,
			Throwable cause) {
		super("Failed to map " + value + " of type " + value.getClass() + " to "
				+ parameter, "Parameter mapping failure", cause);
		this.parameter = parameter;
		this.value = value;
	}

	/**
	 * Return the parameter being mapped.
	 * @return the parameter
	 */
	public OperationParameter getParameter() {
		return this.parameter;
	}

	/**
	 * Return the value being mapped.
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

}
