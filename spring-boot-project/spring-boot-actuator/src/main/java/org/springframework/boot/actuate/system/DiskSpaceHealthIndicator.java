

package org.springframework.boot.actuate.system;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

/**
 * A {@link HealthIndicator} that checks available disk space and reports a status of
 * {@link Status#DOWN} when it drops below a configurable threshold.
 *
 * @author Mattias Severson
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class DiskSpaceHealthIndicator extends AbstractHealthIndicator {

	private static final Log logger = LogFactory.getLog(DiskSpaceHealthIndicator.class);

	private final File path;

	private final long threshold;

	/**
	 * Create a new {@code DiskSpaceHealthIndicator} instance.
	 * @param path the Path used to compute the available disk space
	 * @param threshold the minimum disk space that should be available (in bytes)
	 */
	public DiskSpaceHealthIndicator(File path, long threshold) {
		super("DiskSpace health check failed");
		this.path = path;
		this.threshold = threshold;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		long diskFreeInBytes = this.path.getUsableSpace();
		if (diskFreeInBytes >= this.threshold) {
			builder.up();
		}
		else {
			logger.warn(String.format(
					"Free disk space below threshold. "
							+ "Available: %d bytes (threshold: %d bytes)",
					diskFreeInBytes, this.threshold));
			builder.down();
		}
		builder.withDetail("total", this.path.getTotalSpace())
				.withDetail("free", diskFreeInBytes)
				.withDetail("threshold", this.threshold);
	}

}
