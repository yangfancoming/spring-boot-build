

package org.springframework.boot.autoconfigure.websocket.servlet;

import org.apache.tomcat.websocket.server.WsContextListener;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

/**
 * WebSocket customizer for {@link TomcatServletWebServerFactory}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class TomcatWebSocketServletWebServerCustomizer
		implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>, Ordered {

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.addContextCustomizers((context) -> context
				.addApplicationListener(WsContextListener.class.getName()));
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
