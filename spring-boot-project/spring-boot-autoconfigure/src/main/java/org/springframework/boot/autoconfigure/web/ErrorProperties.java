

package org.springframework.boot.autoconfigure.web;

import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration properties for web error handling.
 *
 * @author Michael Stummvoll
 * @author Stephane Nicoll
 * @author Vedran Pavic
 * @since 1.3.0
 */
public class ErrorProperties {

	/**
	 * Path of the error controller.
	 */
	@Value("${error.path:/error}")
	private String path = "/error";

	/**
	 * Include the "exception" attribute.
	 */
	private boolean includeException;

	/**
	 * When to include a "stacktrace" attribute.
	 */
	private IncludeStacktrace includeStacktrace = IncludeStacktrace.NEVER;

	private Whitelabel whitelabel = new Whitelabel();

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isIncludeException() {
		return this.includeException;
	}

	public void setIncludeException(boolean includeException) {
		this.includeException = includeException;
	}

	public IncludeStacktrace getIncludeStacktrace() {
		return this.includeStacktrace;
	}

	public void setIncludeStacktrace(IncludeStacktrace includeStacktrace) {
		this.includeStacktrace = includeStacktrace;
	}

	public Whitelabel getWhitelabel() {
		return this.whitelabel;
	}

	public void setWhitelabel(Whitelabel whitelabel) {
		this.whitelabel = whitelabel;
	}

	/**
	 * Include Stacktrace attribute options.
	 */
	public enum IncludeStacktrace {

		/**
		 * Never add stacktrace information.
		 */
		NEVER,

		/**
		 * Always add stacktrace information.
		 */
		ALWAYS,

		/**
		 * Add stacktrace information when the "trace" request parameter is "true".
		 */
		ON_TRACE_PARAM

	}

	public static class Whitelabel {

		/**
		 * Whether to enable the default error page displayed in browsers in case of a
		 * server error.
		 */
		private boolean enabled = true;

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

}
