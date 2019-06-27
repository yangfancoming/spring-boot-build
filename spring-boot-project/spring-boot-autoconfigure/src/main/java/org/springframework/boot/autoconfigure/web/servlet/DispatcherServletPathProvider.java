

package org.springframework.boot.autoconfigure.web.servlet;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * Interface that provides the paths that the {@link DispatcherServlet} in an application
 * context is mapped to.
 *
 * @author Madhura Bhave
 * @since 2.0.2
 * @deprecated since 2.0.4 in favor of {@link DispatcherServletPath} and
 * {@link DispatcherServletRegistrationBean}
 */
@Deprecated
@FunctionalInterface
public interface DispatcherServletPathProvider {

	String getServletPath();

}
