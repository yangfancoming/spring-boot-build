

package org.springframework.boot.actuate.endpoint.web;

import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;

/**
 * A {@code WebEndpointResponse} can be returned by an operation on a
 * {@link EndpointWebExtension} to provide additional, web-specific information such as
 * the HTTP status code.
 *
 * @param <T> the type of the response body
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Vedran Pavic
 * @since 2.0.0
 */
public final class WebEndpointResponse<T> {

	/**
	 * {@code 200 OK}.
	 */
	public static final int STATUS_OK = 200;

	/**
	 * {@code 400 Bad Request}.
	 */
	public static final int STATUS_BAD_REQUEST = 400;

	/**
	 * {@code 404 Not Found}.
	 */
	public static final int STATUS_NOT_FOUND = 404;

	/**
	 * {@code 429 Too Many Requests}.
	 */
	public static final int STATUS_TOO_MANY_REQUESTS = 429;

	/**
	 * {@code 500 Internal Server Error}.
	 */
	public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

	/**
	 * {@code 503 Service Unavailable}.
	 */
	public static final int STATUS_SERVICE_UNAVAILABLE = 503;

	private final T body;

	private final int status;

	/**
	 * Creates a new {@code WebEndpointResponse} with no body and a 200 (OK) status.
	 */
	public WebEndpointResponse() {
		this(null);
	}

	/**
	 * Creates a new {@code WebEndpointResponse} with no body and the given
	 * {@code status}.
	 * @param status the HTTP status
	 */
	public WebEndpointResponse(int status) {
		this(null, status);
	}

	/**
	 * Creates a new {@code WebEndpointResponse} with then given body and a 200 (OK)
	 * status.
	 * @param body the body
	 */
	public WebEndpointResponse(T body) {
		this(body, STATUS_OK);
	}

	/**
	 * Creates a new {@code WebEndpointResponse} with then given body and status.
	 * @param body the body
	 * @param status the HTTP status
	 */
	public WebEndpointResponse(T body, int status) {
		this.body = body;
		this.status = status;
	}

	/**
	 * Returns the body for the response.
	 * @return the body
	 */
	public T getBody() {
		return this.body;
	}

	/**
	 * Returns the status for the response.
	 * @return the status
	 */
	public int getStatus() {
		return this.status;
	}

}
