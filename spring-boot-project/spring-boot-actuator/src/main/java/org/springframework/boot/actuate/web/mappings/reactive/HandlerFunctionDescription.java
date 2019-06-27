

package org.springframework.boot.actuate.web.mappings.reactive;

import org.springframework.web.reactive.function.server.HandlerFunction;

/**
 * Description of a {@link HandlerFunction}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class HandlerFunctionDescription {

	private final String className;

	HandlerFunctionDescription(HandlerFunction<?> handlerFunction) {
		this.className = handlerFunction.getClass().getCanonicalName();
	}

	public String getClassName() {
		return this.className;
	}

}
