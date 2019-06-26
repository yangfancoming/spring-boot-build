

package org.springframework.boot.web.servlet.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the server's JSP servlet.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class Jsp {

	/**
	 * The class name of the servlet to use for JSPs. If registered is true and this class
	 * is on the classpath then it will be registered. Since both Tomcat and Jetty use
	 * Jasper for their JSP implementation the default is
	 * org.apache.jasper.servlet.JspServlet.
	 */
	private String className = "org.apache.jasper.servlet.JspServlet";

	/**
	 * Init parameters used to configure the JSP servlet.
	 */
	private Map<String, String> initParameters = new HashMap<>();

	/**
	 * Whether the JSP servlet is registered.
	 */
	private boolean registered = true;

	public Jsp() {
		this.initParameters.put("development", "false");
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getInitParameters() {
		return this.initParameters;
	}

	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

	public boolean getRegistered() {
		return this.registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}
