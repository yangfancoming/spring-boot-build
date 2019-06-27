

package org.springframework.boot.web.context;

import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;

/**
 * Interface to be implemented by {@link ApplicationContext application contexts} that
 * create and manage the lifecycle of an embedded {@link WebServer}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface WebServerApplicationContext extends ApplicationContext {

	/**
	 * Returns the {@link WebServer} that was created by the context or {@code null} if
	 * the server has not yet been created.
	 * @return the web server
	 */
	WebServer getWebServer();

	/**
	 * Returns the namespace of the web server application context or {@code null} if no
	 * namespace has been set. Used for disambiguation when multiple web servers are
	 * running in the same application (for example a management context running on a
	 * different port).
	 * @return the server namespace
	 */
	String getServerNamespace();

}
