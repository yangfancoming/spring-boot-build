

package org.springframework.boot.web.server;

/**
 * A {@code PortInUseException} is thrown when a web server fails to start due to a port
 * already being in use.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class PortInUseException extends WebServerException {

	private final int port;

	/**
	 * Creates a new port in use exception for the given {@code port}.
	 * @param port the port that was in use
	 */
	public PortInUseException(int port) {
		super("Port " + port + " is already in use", null);
		this.port = port;
	}

	/**
	 * Returns the port that was in use.
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

}
