

package org.springframework.boot.actuate.health;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Base {@link HealthIndicator} implementations that encapsulates creation of
 * {@link Health} instance and error handling.
 * <p>
 * This implementation is only suitable if an {@link Exception} raised from
 * {@link #doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)} should
 * create a {@link Status#DOWN} health status.
 *
 * @author Christian Dupuis
 * @since 1.1.0
 */
public abstract class AbstractHealthIndicator implements HealthIndicator {

	private static final String NO_MESSAGE = null;

	private static final String DEFAULT_MESSAGE = "Health check failed";

	private final Log logger = LogFactory.getLog(getClass());

	private final Function<Exception, String> healthCheckFailedMessage;

	/**
	 * Create a new {@link AbstractHealthIndicator} instance with a default
	 * {@code healthCheckFailedMessage}.
	 */
	protected AbstractHealthIndicator() {
		this(NO_MESSAGE);
	}

	/**
	 * Create a new {@link AbstractHealthIndicator} instance with a specific message to
	 * log when the health check fails.
	 * @param healthCheckFailedMessage the message to log on health check failure
	 * @since 2.0.0
	 */
	protected AbstractHealthIndicator(String healthCheckFailedMessage) {
		this.healthCheckFailedMessage = (ex) -> healthCheckFailedMessage;
	}

	/**
	 * Create a new {@link AbstractHealthIndicator} instance with a specific message to
	 * log when the health check fails.
	 * @param healthCheckFailedMessage the message to log on health check failure
	 * @since 2.0.0
	 */
	protected AbstractHealthIndicator(
			Function<Exception, String> healthCheckFailedMessage) {
		Assert.notNull(healthCheckFailedMessage,
				"HealthCheckFailedMessage must not be null");
		this.healthCheckFailedMessage = healthCheckFailedMessage;
	}

	@Override
	public final Health health() {
		Health.Builder builder = new Health.Builder();
		try {
			doHealthCheck(builder);
		}
		catch (Exception ex) {
			if (this.logger.isWarnEnabled()) {
				String message = this.healthCheckFailedMessage.apply(ex);
				this.logger.warn(StringUtils.hasText(message) ? message : DEFAULT_MESSAGE,
						ex);
			}
			builder.down(ex);
		}
		return builder.build();
	}

	/**
	 * Actual health check logic.
	 * @param builder the {@link Builder} to report health status and details
	 * @throws Exception any {@link Exception} that should create a {@link Status#DOWN}
	 * system status.
	 */
	protected abstract void doHealthCheck(Health.Builder builder) throws Exception;

}
