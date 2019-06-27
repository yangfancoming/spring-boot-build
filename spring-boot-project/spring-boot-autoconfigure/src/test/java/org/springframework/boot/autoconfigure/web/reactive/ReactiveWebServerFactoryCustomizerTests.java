

package org.springframework.boot.autoconfigure.web.reactive;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.server.ConfigurableReactiveWebServerFactory;
import org.springframework.boot.web.server.Ssl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ReactiveWebServerFactoryCustomizer}.
 *
 * @author Brian Clozel
 * @author Yunkun Huang
 */
public class ReactiveWebServerFactoryCustomizerTests {

	private ServerProperties properties = new ServerProperties();

	private ReactiveWebServerFactoryCustomizer customizer;

	@Before
	public void setup() {
		this.customizer = new ReactiveWebServerFactoryCustomizer(this.properties);
	}

	@Test
	public void testCustomizeServerPort() {
		ConfigurableReactiveWebServerFactory factory = mock(
				ConfigurableReactiveWebServerFactory.class);
		this.properties.setPort(9000);
		this.customizer.customize(factory);
		verify(factory).setPort(9000);
	}

	@Test
	public void testCustomizeServerAddress() {
		ConfigurableReactiveWebServerFactory factory = mock(
				ConfigurableReactiveWebServerFactory.class);
		InetAddress address = mock(InetAddress.class);
		this.properties.setAddress(address);
		this.customizer.customize(factory);
		verify(factory).setAddress(address);
	}

	@Test
	public void testCustomizeServerSsl() {
		ConfigurableReactiveWebServerFactory factory = mock(
				ConfigurableReactiveWebServerFactory.class);
		Ssl ssl = mock(Ssl.class);
		this.properties.setSsl(ssl);
		this.customizer.customize(factory);
		verify(factory).setSsl(ssl);
	}

}
