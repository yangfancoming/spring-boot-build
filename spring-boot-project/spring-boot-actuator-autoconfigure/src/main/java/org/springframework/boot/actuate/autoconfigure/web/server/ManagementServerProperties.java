

package org.springframework.boot.actuate.autoconfigure.web.server;

import java.net.InetAddress;

import org.springframework.boot.autoconfigure.security.SecurityPrerequisite;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.server.Ssl;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Properties for the management server (e.g. port and path settings).
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 * @author Vedran Pavic
 * @since 2.0.0
 * @see ServerProperties
 */
@ConfigurationProperties(prefix = "management.server", ignoreUnknownFields = true)
public class ManagementServerProperties implements SecurityPrerequisite {

	/**
	 * Management endpoint HTTP port (uses the same port as the application by default).
	 * Configure a different port to use management-specific SSL.
	 */
	private Integer port;

	/**
	 * Network address to which the management endpoints should bind. Requires a custom
	 * management.server.port.
	 */
	private InetAddress address;

	private final Servlet servlet = new Servlet();

	@NestedConfigurationProperty
	private Ssl ssl;

	/**
	 * Returns the management port or {@code null} if the
	 * {@link ServerProperties#getPort() server port} should be used.
	 * @return the port
	 * @see #setPort(Integer)
	 */
	public Integer getPort() {
		return this.port;
	}

	/**
	 * Sets the port of the management server, use {@code null} if the
	 * {@link ServerProperties#getPort() server port} should be used. To disable use 0.
	 * @param port the port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public Ssl getSsl() {
		return this.ssl;
	}

	public void setSsl(Ssl ssl) {
		this.ssl = ssl;
	}

	public Servlet getServlet() {
		return this.servlet;
	}

	/**
	 * Servlet properties.
	 */
	public static class Servlet {

		/**
		 * Management endpoint context-path (for instance, `/management`). Requires a
		 * custom management.server.port.
		 */
		private String contextPath = "";

		/**
		 * Return the context path with no trailing slash (i.e. the '/' root context is
		 * represented as the empty string).
		 * @return the context path (no trailing slash)
		 */
		public String getContextPath() {
			return this.contextPath;
		}

		public void setContextPath(String contextPath) {
			Assert.notNull(contextPath, "ContextPath must not be null");
			this.contextPath = cleanContextPath(contextPath);
		}

		private String cleanContextPath(String contextPath) {
			if (StringUtils.hasText(contextPath) && contextPath.endsWith("/")) {
				return contextPath.substring(0, contextPath.length() - 1);
			}
			return contextPath;
		}

	}

}
