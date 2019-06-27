

package org.springframework.boot.web.embedded.netty;

import java.time.Duration;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InOrder;
import reactor.ipc.netty.http.server.HttpServerOptions;

import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactoryTests;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link NettyReactiveWebServerFactory}.
 *
 * @author Brian Clozel
 */
public class NettyReactiveWebServerFactoryTests
		extends AbstractReactiveWebServerFactoryTests {

	@Override
	protected NettyReactiveWebServerFactory getFactory() {
		return new NettyReactiveWebServerFactory(0);
	}

	@Test
	public void exceptionIsThrownWhenPortIsAlreadyInUse() {
		AbstractReactiveWebServerFactory factory = getFactory();
		factory.setPort(0);
		this.webServer = factory.getWebServer(new EchoHandler());
		this.webServer.start();
		factory.setPort(this.webServer.getPort());
		this.thrown.expect(WebServerException.class);
		factory.getWebServer(new EchoHandler()).start();
	}

	@Test
	public void nettyCustomizers() {
		NettyReactiveWebServerFactory factory = getFactory();
		NettyServerCustomizer[] customizers = new NettyServerCustomizer[2];
		for (int i = 0; i < customizers.length; i++) {
			customizers[i] = mock(NettyServerCustomizer.class);
		}
		factory.setServerCustomizers(Arrays.asList(customizers[0], customizers[1]));
		this.webServer = factory.getWebServer(new EchoHandler());
		InOrder ordered = inOrder((Object[]) customizers);
		for (NettyServerCustomizer customizer : customizers) {
			ordered.verify(customizer).customize(any(HttpServerOptions.Builder.class));
		}
	}

	@Test
	public void customStartupTimeout() {
		Duration timeout = Duration.ofDays(365);
		NettyReactiveWebServerFactory factory = getFactory();
		factory.setLifecycleTimeout(timeout);
		this.webServer = factory.getWebServer(new EchoHandler());
		this.webServer.start();
		Object context = ReflectionTestUtils.getField(this.webServer, "nettyContext");
		Object actualTimeout = ReflectionTestUtils.getField(context, "lifecycleTimeout");
		assertThat(actualTimeout).isEqualTo(timeout);
	}

}
