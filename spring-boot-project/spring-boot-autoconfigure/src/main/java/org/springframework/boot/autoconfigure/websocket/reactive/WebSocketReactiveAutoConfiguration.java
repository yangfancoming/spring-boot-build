

package org.springframework.boot.autoconfigure.websocket.reactive;

import javax.servlet.Servlet;
import javax.websocket.server.ServerContainer;

import org.apache.catalina.startup.Tomcat;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configuration for WebSocket reactive server in Tomcat, Jetty or Undertow. Requires
 * the appropriate WebSocket modules to be on the classpath.
 * <p>
 * If Tomcat's WebSocket support is detected on the classpath we add a customizer that
 * installs the Tomcat WebSocket initializer.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass({ Servlet.class, ServerContainer.class })
@ConditionalOnWebApplication(type = Type.REACTIVE)
@AutoConfigureBefore(ReactiveWebServerFactoryAutoConfiguration.class)
public class WebSocketReactiveAutoConfiguration {

	@Configuration
	@ConditionalOnClass(name = "org.apache.tomcat.websocket.server.WsSci", value = Tomcat.class)
	static class TomcatWebSocketConfiguration {

		@Bean
		@ConditionalOnMissingBean(name = "websocketReactiveWebServerCustomizer")
		public TomcatWebSocketReactiveWebServerCustomizer websocketContainerCustomizer() {
			return new TomcatWebSocketReactiveWebServerCustomizer();
		}

	}

}
