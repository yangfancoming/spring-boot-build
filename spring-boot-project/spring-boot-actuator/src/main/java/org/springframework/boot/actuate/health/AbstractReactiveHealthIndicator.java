

package org.springframework.boot.actuate.health;

import reactor.core.publisher.Mono;

/**
 * Base {@link ReactiveHealthIndicator} implementations that encapsulates creation of
 * {@link Health} instance and error handling.
 *
 * @author Stephane Nicoll
 * @author Nikolay Rybak
 * @since 2.0.0
 */
public abstract class AbstractReactiveHealthIndicator implements ReactiveHealthIndicator {

	@Override
	public final Mono<Health> health() {
		try {
			return doHealthCheck(new Health.Builder()).onErrorResume(this::handleFailure);
		}
		catch (Exception ex) {
			return handleFailure(ex);
		}
	}

	private Mono<Health> handleFailure(Throwable ex) {
		return Mono.just(new Health.Builder().down(ex).build());
	}

	/**
	 * Actual health check logic. If an error occurs in the pipeline it will be handled
	 * automatically.
	 * @param builder the {@link Health.Builder} to report health status and details
	 * @return a {@link Mono} that provides the {@link Health}
	 */
	protected abstract Mono<Health> doHealthCheck(Health.Builder builder);

}
