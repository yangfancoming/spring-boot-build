

package org.springframework.boot.actuate.autoconfigure.web.server;

import org.springframework.core.env.Environment;

/**
 * Port types that can be used to control how the management server is started.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public enum ManagementPortType {

	/**
	 * The management port has been disabled.
	 */
	DISABLED,

	/**
	 * The management port is the same as the server port.
	 */
	SAME,

	/**
	 * The management port and server port are different.
	 */
	DIFFERENT;

	static ManagementPortType get(Environment environment) {
		Integer serverPort = getPortProperty(environment, "server.");
		Integer managementPort = getPortProperty(environment, "management.server.");
		if (managementPort != null && managementPort < 0) {
			return DISABLED;
		}
		return ((managementPort == null
				|| (serverPort == null && managementPort.equals(8080))
				|| (managementPort != 0 && managementPort.equals(serverPort))) ? SAME
						: DIFFERENT);
	}

	private static Integer getPortProperty(Environment environment, String prefix) {
		return environment.getProperty(prefix + "port", Integer.class);
	}

}
