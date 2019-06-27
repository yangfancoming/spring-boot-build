

package org.springframework.boot.web.server;

/**
 * Simple server-independent abstraction for HTTP/2 configuration.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
public class Http2 {

	/**
	 * Whether to enable HTTP/2 support, if the current environment supports it.
	 */
	private boolean enabled = false;

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
