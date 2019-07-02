

package org.springframework.boot.autoconfigure.info;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Configuration properties for project information.
 *
 * @author Stephane Nicoll
 * @since 1.4.0
 */
@ConfigurationProperties(prefix = "spring.info")
public class ProjectInfoProperties {

	private final Build build = new Build();

	private final Git git = new Git();

	public Build getBuild() {
		return this.build;
	}

	public Git getGit() {
		return this.git;
	}

	/**
	 * Build specific info properties.
	 */
	public static class Build {

		/**
		 * Location of the generated build-info.properties file.
		 */
		private Resource location = new ClassPathResource(
				"META-INF/build-info.properties");

		public Resource getLocation() {
			return this.location;
		}

		public void setLocation(Resource location) {
			this.location = location;
		}

	}

	/**
	 * Git specific info properties.
	 */
	public static class Git {

		/**
		 * Location of the generated git.properties file.
		 */
		private Resource location = new ClassPathResource("git.properties");

		public Resource getLocation() {
			return this.location;
		}

		public void setLocation(Resource location) {
			this.location = location;
		}

	}

}