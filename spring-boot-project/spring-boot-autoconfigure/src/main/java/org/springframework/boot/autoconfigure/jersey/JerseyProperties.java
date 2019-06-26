

package org.springframework.boot.autoconfigure.jersey;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for Jersey.
 *
 * @author Dave Syer
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @since 1.2.0
 */
@ConfigurationProperties(prefix = "spring.jersey")
public class JerseyProperties {

	/**
	 * Jersey integration type.
	 */
	private Type type = Type.SERVLET;

	/**
	 * Init parameters to pass to Jersey through the servlet or filter.
	 */
	private Map<String, String> init = new HashMap<>();

	private final Filter filter = new Filter();

	private final Servlet servlet = new Servlet();

	/**
	 * Path that serves as the base URI for the application. If specified, overrides the
	 * value of "@ApplicationPath".
	 */
	private String applicationPath;

	public Filter getFilter() {
		return this.filter;
	}

	public Servlet getServlet() {
		return this.servlet;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Map<String, String> getInit() {
		return this.init;
	}

	public void setInit(Map<String, String> init) {
		this.init = init;
	}

	public String getApplicationPath() {
		return this.applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}

	public enum Type {

		SERVLET, FILTER

	}

	public static class Filter {

		/**
		 * Jersey filter chain order.
		 */
		private int order;

		public int getOrder() {
			return this.order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

	}

	public static class Servlet {

		/**
		 * Load on startup priority of the Jersey servlet.
		 */
		private int loadOnStartup = -1;

		public int getLoadOnStartup() {
			return this.loadOnStartup;
		}

		public void setLoadOnStartup(int loadOnStartup) {
			this.loadOnStartup = loadOnStartup;
		}

	}

}
