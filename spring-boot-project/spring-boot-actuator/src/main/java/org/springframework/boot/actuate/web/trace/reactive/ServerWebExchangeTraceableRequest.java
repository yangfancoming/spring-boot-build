

package org.springframework.boot.actuate.web.trace.reactive;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.trace.http.TraceableRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * A {@link TraceableRequest} backed by a {@link ServerWebExchange}.
 *
 * @author Andy Wilkinson
 */
class ServerWebExchangeTraceableRequest implements TraceableRequest {

	private final String method;

	private final Map<String, List<String>> headers;

	private final URI uri;

	private final String remoteAddress;

	ServerWebExchangeTraceableRequest(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		this.method = request.getMethodValue();
		this.headers = request.getHeaders();
		this.uri = request.getURI();
		this.remoteAddress = (request.getRemoteAddress() != null)
				? request.getRemoteAddress().getAddress().toString() : null;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public URI getUri() {
		return this.uri;
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return new LinkedHashMap<>(this.headers);
	}

	@Override
	public String getRemoteAddress() {
		return this.remoteAddress;
	}

}
