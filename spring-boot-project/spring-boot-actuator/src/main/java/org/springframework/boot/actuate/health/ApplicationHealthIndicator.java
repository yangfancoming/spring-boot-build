

package org.springframework.boot.actuate.health;

/**
 * Default implementation of {@link HealthIndicator} that returns {@link Status#UP}.
 *
 * @author Dave Syer
 * @author Christian Dupuis
 * @see Status#UP
 */
public class ApplicationHealthIndicator extends AbstractHealthIndicator {

	public ApplicationHealthIndicator() {
		super("Application health check failed");
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		builder.up();
	}

}
