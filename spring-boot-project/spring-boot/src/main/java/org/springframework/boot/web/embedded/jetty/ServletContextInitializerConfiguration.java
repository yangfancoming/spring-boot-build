

package org.springframework.boot.web.embedded.jetty;

import javax.servlet.ServletException;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.util.Assert;

/**
 * Jetty {@link Configuration} that calls {@link ServletContextInitializer}s.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class ServletContextInitializerConfiguration extends AbstractConfiguration {

	private final ServletContextInitializer[] initializers;

	/**
	 * Create a new {@link ServletContextInitializerConfiguration}.
	 * @param initializers the initializers that should be invoked
	 * @since 1.2.1
	 */
	public ServletContextInitializerConfiguration(
			ServletContextInitializer... initializers) {
		Assert.notNull(initializers, "Initializers must not be null");
		this.initializers = initializers;
	}

	@Override
	public void configure(WebAppContext context) throws Exception {
		context.addBean(new Initializer(context), true);
	}

	/**
	 * Jetty {@link AbstractLifeCycle} to call the {@link ServletContextInitializer
	 * ServletContextInitializers}.
	 */
	private class Initializer extends AbstractLifeCycle {

		private final WebAppContext context;

		Initializer(WebAppContext context) {
			this.context = context;
		}

		@Override
		protected void doStart() throws Exception {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(this.context.getClassLoader());
			try {
				callInitializers();
			}
			finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
		}

		private void callInitializers() throws ServletException {
			try {
				setExtendedListenerTypes(true);
				for (ServletContextInitializer initializer : ServletContextInitializerConfiguration.this.initializers) {
					initializer.onStartup(this.context.getServletContext());
				}
			}
			finally {
				setExtendedListenerTypes(false);
			}
		}

		private void setExtendedListenerTypes(boolean extended) {
			try {
				this.context.getServletContext().setExtendedListenerTypes(extended);
			}
			catch (NoSuchMethodError ex) {
				// Not available on Jetty 8
			}
		}

	}

}
