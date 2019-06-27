

package org.springframework.boot.web.reactive.error;

import java.util.Map;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * Provides access to error attributes which can be logged or presented to the user.
 *
 * @author Brian Clozel
 * @since 2.0
 * @see DefaultErrorAttributes
 */
public interface ErrorAttributes {

	/**
	 * Return a {@link Map} of the error attributes. The map can be used as the model of
	 * an error page, or returned as a {@link ServerResponse} body.
	 * @param request the source request
	 * @param includeStackTrace if stack trace elements should be included
	 * @return a map of error attributes
	 */
	Map<String, Object> getErrorAttributes(ServerRequest request,
			boolean includeStackTrace);

	/**
	 * Return the underlying cause of the error or {@code null} if the error cannot be
	 * extracted.
	 * @param request the source ServerRequest
	 * @return the {@link Exception} that caused the error or {@code null}
	 */
	Throwable getError(ServerRequest request);

	/**
	 * Store the given error information in the current {@link ServerWebExchange}.
	 * @param error the {@link Exception} that caused the error
	 * @param exchange the source exchange
	 */
	void storeErrorInformation(Throwable error, ServerWebExchange exchange);

}
