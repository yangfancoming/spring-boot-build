

package org.springframework.boot.web.reactive.server;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;

/**
 * Configurable {@link ReactiveWebServerFactory}.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
public interface ConfigurableReactiveWebServerFactory
		extends ConfigurableWebServerFactory, ReactiveWebServerFactory {

}
