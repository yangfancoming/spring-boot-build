

package org.springframework.boot.actuate.health;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Health}.
 *
 * @author Phillip Webb
 */
public class HealthTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void statusMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Status must not be null");
		new Health.Builder(null, null);
	}

	@Test
	public void createWithStatus() {
		Health health = Health.status(Status.UP).build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void createWithDetails() {
		Health health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b"))
				.build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails().get("a")).isEqualTo("b");
	}

	@Test
	public void equalsAndHashCode() {
		Health h1 = new Health.Builder(Status.UP, Collections.singletonMap("a", "b"))
				.build();
		Health h2 = new Health.Builder(Status.UP, Collections.singletonMap("a", "b"))
				.build();
		Health h3 = new Health.Builder(Status.UP).build();
		assertThat(h1).isEqualTo(h1);
		assertThat(h1).isEqualTo(h2);
		assertThat(h1).isNotEqualTo(h3);
		assertThat(h1.hashCode()).isEqualTo(h1.hashCode());
		assertThat(h1.hashCode()).isEqualTo(h2.hashCode());
		assertThat(h1.hashCode()).isNotEqualTo(h3.hashCode());
	}

	@Test
	public void withException() {
		RuntimeException ex = new RuntimeException("bang");
		Health health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b"))
				.withException(ex).build();
		assertThat(health.getDetails().get("a")).isEqualTo("b");
		assertThat(health.getDetails().get("error"))
				.isEqualTo("java.lang.RuntimeException: bang");
	}

	@Test
	public void withDetails() {
		Health health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b"))
				.withDetail("c", "d").build();
		assertThat(health.getDetails().get("a")).isEqualTo("b");
		assertThat(health.getDetails().get("c")).isEqualTo("d");
	}

	@Test
	public void unknownWithDetails() {
		Health health = new Health.Builder().unknown().withDetail("a", "b").build();
		assertThat(health.getStatus()).isEqualTo(Status.UNKNOWN);
		assertThat(health.getDetails().get("a")).isEqualTo("b");
	}

	@Test
	public void unknown() {
		Health health = new Health.Builder().unknown().build();
		assertThat(health.getStatus()).isEqualTo(Status.UNKNOWN);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void upWithDetails() {
		Health health = new Health.Builder().up().withDetail("a", "b").build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails().get("a")).isEqualTo("b");
	}

	@Test
	public void up() {
		Health health = new Health.Builder().up().build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void downWithException() {
		RuntimeException ex = new RuntimeException("bang");
		Health health = Health.down(ex).build();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails().get("error"))
				.isEqualTo("java.lang.RuntimeException: bang");
	}

	@Test
	public void down() {
		Health health = Health.down().build();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void outOfService() {
		Health health = Health.outOfService().build();
		assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void statusCode() {
		Health health = Health.status("UP").build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	public void status() {
		Health health = Health.status(Status.UP).build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

}
