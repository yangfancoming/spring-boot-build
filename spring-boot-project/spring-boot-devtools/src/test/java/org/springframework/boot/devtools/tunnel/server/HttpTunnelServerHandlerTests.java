

package org.springframework.boot.devtools.tunnel.server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link HttpTunnelServerHandler}.
 *
 * @author Phillip Webb
 */
public class HttpTunnelServerHandlerTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void serverMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Server must not be null");
		new HttpTunnelServerHandler(null);
	}

	@Test
	public void handleDelegatesToServer() throws Exception {
		HttpTunnelServer server = mock(HttpTunnelServer.class);
		HttpTunnelServerHandler handler = new HttpTunnelServerHandler(server);
		ServerHttpRequest request = mock(ServerHttpRequest.class);
		ServerHttpResponse response = mock(ServerHttpResponse.class);
		handler.handle(request, response);
		verify(server).handle(request, response);
	}

}
