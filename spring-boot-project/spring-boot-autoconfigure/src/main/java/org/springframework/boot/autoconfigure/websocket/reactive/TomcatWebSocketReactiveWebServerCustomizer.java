

package org.springframework.boot.autoconfigure.websocket.reactive;

import org.apache.tomcat.websocket.server.WsContextListener;

import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

/**
 * WebSocket customizer for {@link TomcatReactiveWebServerFactory}.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
public class TomcatWebSocketReactiveWebServerCustomizer
		implements WebServerFactoryCustomizer<TomcatReactiveWebServerFactory>, Ordered {

	@Override
	public void customize(TomcatReactiveWebServerFactory factory) {
		factory.addContextCustomizers((context) -> context
				.addApplicationListener(WsContextListener.class.getName()));
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
