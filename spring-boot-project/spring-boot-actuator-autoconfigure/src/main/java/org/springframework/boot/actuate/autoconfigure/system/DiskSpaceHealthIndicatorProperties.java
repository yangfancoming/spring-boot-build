

package org.springframework.boot.actuate.autoconfigure.system;

import java.io.File;

import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * External configuration properties for {@link DiskSpaceHealthIndicator}.
 *
 * @author Andy Wilkinson
 * @since 1.2.0
 */
@ConfigurationProperties(prefix = "management.health.diskspace")
public class DiskSpaceHealthIndicatorProperties {

	private static final int MEGABYTES = 1024 * 1024;

	private static final int DEFAULT_THRESHOLD = 10 * MEGABYTES;

	/**
	 * Path used to compute the available disk space.
	 */
	private File path = new File(".");

	/**
	 * Minimum disk space, in bytes, that should be available.
	 */
	private long threshold = DEFAULT_THRESHOLD;

	public File getPath() {
		return this.path;
	}

	public void setPath(File path) {
		Assert.isTrue(path.exists(), () -> "Path '" + path + "' does not exist");
		Assert.isTrue(path.canRead(), () -> "Path '" + path + "' cannot be read");
		this.path = path;
	}

	public long getThreshold() {
		return this.threshold;
	}

	public void setThreshold(long threshold) {
		Assert.isTrue(threshold >= 0, "threshold must be greater than 0");
		this.threshold = threshold;
	}

}
