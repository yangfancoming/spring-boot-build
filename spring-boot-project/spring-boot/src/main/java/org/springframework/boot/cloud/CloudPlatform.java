

package org.springframework.boot.cloud;

import org.springframework.core.env.Environment;

/**
 * Simple detection for well known cloud platforms. For more advanced cloud provider
 * integration consider the Spring Cloud project.
 *
 * @author Phillip Webb
 * @since 1.3.0
 * @see "http://cloud.spring.io"
 */
public enum CloudPlatform {

	/**
	 * Cloud Foundry platform.
	 */
	CLOUD_FOUNDRY {

		@Override
		public boolean isActive(Environment environment) {
			return environment.containsProperty("VCAP_APPLICATION")
					|| environment.containsProperty("VCAP_SERVICES");
		}

	},

	/**
	 * Heroku platform.
	 */
	HEROKU {

		@Override
		public boolean isActive(Environment environment) {
			return environment.containsProperty("DYNO");
		}

	},

	/**
	 * SAP Cloud platform.
	 */
	SAP {

		@Override
		public boolean isActive(Environment environment) {
			return environment.containsProperty("HC_LANDSCAPE");
		}

	};

	/**
	 * Determines if the platform is active (i.e. the application is running in it).
	 * @param environment the environment
	 * @return if the platform is active.
	 */
	public abstract boolean isActive(Environment environment);

	/**
	 * Returns if the platform is behind a load balancer and uses
	 * {@literal X-Forwarded-For} headers.
	 * @return if {@literal X-Forwarded-For} headers are used
	 */
	public boolean isUsingForwardHeaders() {
		return true;
	}

	/**
	 * Returns the active {@link CloudPlatform} or {@code null} if one cannot be deduced.
	 * @param environment the environment
	 * @return the {@link CloudPlatform} or {@code null}
	 */
	public static CloudPlatform getActive(Environment environment) {
		if (environment != null) {
			for (CloudPlatform cloudPlatform : values()) {
				if (cloudPlatform.isActive(environment)) {
					return cloudPlatform;
				}
			}
		}
		return null;
	}

}
