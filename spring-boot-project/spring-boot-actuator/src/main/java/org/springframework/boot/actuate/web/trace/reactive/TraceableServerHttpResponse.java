

package org.springframework.boot.actuate.web.trace.reactive;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.trace.http.TraceableResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * An adapter that exposes a {@link ServerHttpResponse} as a {@link TraceableResponse}.
 *
 * @author Andy Wilkinson
 */
class TraceableServerHttpResponse implements TraceableResponse {

	private final ServerHttpResponse response;

	TraceableServerHttpResponse(ServerHttpResponse exchange) {
		this.response = exchange;
	}

	@Override
	public int getStatus() {
		return (this.response.getStatusCode() != null)
				? this.response.getStatusCode().value() : 200;
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return new LinkedHashMap<>(this.response.getHeaders());
	}

}
