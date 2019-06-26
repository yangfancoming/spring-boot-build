

package org.springframework.boot.autoconfigure.websocket.servlet;

import javax.websocket.server.ServerContainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebSocketServletAutoConfiguration}
 *
 * @author Andy Wilkinson
 */
public class WebSocketServletAutoConfigurationTests {

	private AnnotationConfigServletWebServerApplicationContext context;

	@Before
	public void createContext() {
		this.context = new AnnotationConfigServletWebServerApplicationContext();
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void tomcatServerContainerIsAvailableFromTheServletContext() {
		serverContainerIsAvailableFromTheServletContext(TomcatConfiguration.class,
				WebSocketServletAutoConfiguration.TomcatWebSocketConfiguration.class);
	}

	@Test
	public void jettyServerContainerIsAvailableFromTheServletContext() {
		serverContainerIsAvailableFromTheServletContext(JettyConfiguration.class,
				WebSocketServletAutoConfiguration.JettyWebSocketConfiguration.class);
	}

	private void serverContainerIsAvailableFromTheServletContext(
			Class<?>... configuration) {
		this.context.register(configuration);
		this.context.refresh();
		Object serverContainer = this.context.getServletContext()
				.getAttribute("javax.websocket.server.ServerContainer");
		assertThat(serverContainer).isInstanceOf(ServerContainer.class);

	}

	static class CommonConfiguration {

		@Bean
		public WebServerFactoryCustomizerBeanPostProcessor ServletWebServerCustomizerBeanPostProcessor() {
			return new WebServerFactoryCustomizerBeanPostProcessor();
		}

	}

	@Configuration
	static class TomcatConfiguration extends CommonConfiguration {

		@Bean
		public ServletWebServerFactory webServerFactory() {
			TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			factory.setPort(0);
			return factory;
		}

	}

	@Configuration
	static class JettyConfiguration extends CommonConfiguration {

		@Bean
		public ServletWebServerFactory webServerFactory() {
			JettyServletWebServerFactory JettyServletWebServerFactory = new JettyServletWebServerFactory();
			JettyServletWebServerFactory.setPort(0);
			return JettyServletWebServerFactory;
		}

	}

}
