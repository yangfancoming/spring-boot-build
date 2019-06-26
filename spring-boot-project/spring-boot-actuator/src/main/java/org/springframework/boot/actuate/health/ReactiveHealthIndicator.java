

package org.springframework.boot.actuate.health;

import reactor.core.publisher.Mono;

/**
 * Defines the {@link Health} of an arbitrary system or component.
 * <p>
 * This is non blocking contract that is meant to be used in a reactive application. See
 * {@link HealthIndicator} for the traditional contract.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 * @see HealthIndicator
 */
@FunctionalInterface
public interface ReactiveHealthIndicator {

	/**
	 * Provide the indicator of health.
	 * @return a {@link Mono} that provides the {@link Health}
	 */
	Mono<Health> health();

}
