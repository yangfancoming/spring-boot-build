

package org.springframework.boot.web.embedded.jetty;

import org.eclipse.jetty.server.Server;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;

/**
 * {@link ConfigurableWebServerFactory} for Jetty-specific features.
 *
 * @author Brian Clozel
 * @since 2.0.0
 * @see JettyServletWebServerFactory
 * @see JettyReactiveWebServerFactory
 */
public interface ConfigurableJettyWebServerFactory extends ConfigurableWebServerFactory {

	/**
	 * Set the number of acceptor threads to use.
	 * @param acceptors the number of acceptor threads to use
	 */
	void setAcceptors(int acceptors);

	/**
	 * Set the number of selector threads to use.
	 * @param selectors the number of selector threads to use
	 */
	void setSelectors(int selectors);

	/**
	 * Set if x-forward-* headers should be processed.
	 * @param useForwardHeaders if x-forward headers should be used
	 */
	void setUseForwardHeaders(boolean useForwardHeaders);

	/**
	 * Add {@link JettyServerCustomizer}s that will be applied to the {@link Server}
	 * before it is started.
	 * @param customizers the customizers to add
	 */
	void addServerCustomizers(JettyServerCustomizer... customizers);

}
