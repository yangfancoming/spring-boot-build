

package org.springframework.boot.autoconfigure.reactor.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for Reactor Core.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "spring.reactor")
public class ReactorCoreProperties {

	private final StacktraceMode stacktraceMode = new StacktraceMode();

	public StacktraceMode getStacktraceMode() {
		return this.stacktraceMode;
	}

	public static class StacktraceMode {

		/**
		 * Whether Reactor should collect stacktrace information at runtime.
		 */
		private boolean enabled;

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

}
