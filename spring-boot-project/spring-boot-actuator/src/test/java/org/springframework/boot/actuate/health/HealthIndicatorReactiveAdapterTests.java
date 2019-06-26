

package org.springframework.boot.actuate.health;

import org.junit.Test;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link HealthIndicatorReactiveAdapter}.
 *
 * @author Stephane Nicoll
 */
public class HealthIndicatorReactiveAdapterTests {

	@Test
	public void delegateReturnsHealth() {
		HealthIndicator delegate = mock(HealthIndicator.class);
		HealthIndicatorReactiveAdapter adapter = new HealthIndicatorReactiveAdapter(
				delegate);
		Health status = Health.up().build();
		given(delegate.health()).willReturn(status);
		StepVerifier.create(adapter.health()).expectNext(status).verifyComplete();
	}

	@Test
	public void delegateThrowError() {
		HealthIndicator delegate = mock(HealthIndicator.class);
		HealthIndicatorReactiveAdapter adapter = new HealthIndicatorReactiveAdapter(
				delegate);
		given(delegate.health()).willThrow(new IllegalStateException("Expected"));
		StepVerifier.create(adapter.health()).expectError(IllegalStateException.class);
	}

	@Test
	public void delegateRunsOnTheElasticScheduler() {
		String currentThread = Thread.currentThread().getName();
		HealthIndicator delegate = () -> Health
				.status(Thread.currentThread().getName().equals(currentThread)
						? Status.DOWN : Status.UP)
				.build();
		HealthIndicatorReactiveAdapter adapter = new HealthIndicatorReactiveAdapter(
				delegate);
		StepVerifier.create(adapter.health()).expectNext(Health.status(Status.UP).build())
				.verifyComplete();
	}

}
