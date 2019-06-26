

package org.springframework.boot.actuate.health;

/**
 * Strategy interface used to provide an indication of application health.
 *
 * @author Dave Syer
 * @see ApplicationHealthIndicator
 */
@FunctionalInterface
public interface HealthIndicator {

	/**
	 * Return an indication of health.
	 * @return the health for
	 */
	Health health();

}
