

package org.springframework.boot.web.reactive.context;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;

/**
 * Event to be published after the {@link ReactiveWebServerApplicationContext} is
 * refreshed and the {@link WebServer} is ready. Useful for obtaining the local port of a
 * running server.
 *
 * @author Brian Clozel
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class ReactiveWebServerInitializedEvent extends WebServerInitializedEvent {

	private final ReactiveWebServerApplicationContext applicationContext;

	public ReactiveWebServerInitializedEvent(WebServer webServer,
			ReactiveWebServerApplicationContext applicationContext) {
		super(webServer);
		this.applicationContext = applicationContext;
	}

	@Override
	public ReactiveWebServerApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

}
