

package org.springframework.boot.web.embedded.netty;

import reactor.ipc.netty.http.server.HttpServerOptions;

/**
 * Callback interface that can be used to customize a Reactor Netty server builder.
 *
 * @author Brian Clozel
 * @see NettyReactiveWebServerFactory
 * @since 2.0.0
 */
@FunctionalInterface
public interface NettyServerCustomizer {

	/**
	 * Customize the Netty web server.
	 * @param builder the server options builder to customize
	 */
	void customize(HttpServerOptions.Builder builder);

}
