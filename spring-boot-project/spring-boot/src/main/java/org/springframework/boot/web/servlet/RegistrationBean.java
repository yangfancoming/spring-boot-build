

package org.springframework.boot.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

/**
 * Base class for Servlet 3.0+ based registration beans.
 * @since 1.4.0
 * @see ServletRegistrationBean
 * @see FilterRegistrationBean
 * @see DelegatingFilterProxyRegistrationBean
 * @see ServletListenerRegistrationBean
 */
public abstract class RegistrationBean implements ServletContextInitializer, Ordered {

	private static final Log logger = LogFactory.getLog(RegistrationBean.class);

	private int order = Ordered.LOWEST_PRECEDENCE;

	private boolean enabled = true;

	@Override
	public final void onStartup(ServletContext servletContext) throws ServletException {
		String description = getDescription();
		if (!isEnabled()) {
			logger.info(StringUtils.capitalize(description) + " was not registered (disabled)");
			return;
		}
		register(description, servletContext);
	}

	/**
	 * Return a description of the registration. For example "Servlet resourceServlet"
	 * @return a description of the registration
	 */
	protected abstract String getDescription();

	/**
	 * Register this bean with the servlet context.
	 * @param description a description of the item being registered
	 * @param servletContext the servlet context
	 */
	protected abstract void register(String description, ServletContext servletContext);

	/**
	 * Flag to indicate that the registration is enabled.
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Return if the registration is enabled.
	 * @return if enabled (default {@code true})
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Set the order of the registration bean.
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Get the order of the registration bean.
	 * @return the order
	 */
	@Override
	public int getOrder() {
		return this.order;
	}
}
