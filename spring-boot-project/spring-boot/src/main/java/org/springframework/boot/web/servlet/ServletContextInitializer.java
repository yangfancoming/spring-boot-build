

package org.springframework.boot.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * Interface used to configure a Servlet 3.0+ {@link ServletContext context} programmatically.
 * Unlike {@link WebApplicationInitializer}, classes that implement this interface (and do not implement {@link WebApplicationInitializer}) will <b>not</b> be
 * detected by {@link SpringServletContainerInitializer} and hence will not be automatically bootstrapped by the Servlet container.
 * This interface is primarily designed to allow {@link ServletContextInitializer}s to bemanaged by Spring and not the Servlet container.
 * For configuration examples see {@link WebApplicationInitializer}.
 * @since 1.4.0
 * @see WebApplicationInitializer
 */
@FunctionalInterface
public interface ServletContextInitializer {

	/**
	 * Configure the given {@link ServletContext} with any servlets, filters, listeners context-params and attributes necessary for initialization.
	 * @param servletContext the {@code ServletContext} to initialize
	 * @throws ServletException if any call against the given {@code ServletContext} throws a {@code ServletException}
	 */
	void onStartup(ServletContext servletContext) throws ServletException;
}
