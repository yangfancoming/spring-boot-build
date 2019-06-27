

package org.springframework.boot.origin;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SystemEnvironmentOrigin}.
 *
 * @author Madhura Bhave
 */
public class SystemEnvironmentOriginTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenPropertyIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		new SystemEnvironmentOrigin(null);
	}

	@Test
	public void createWhenPropertyNameIsEmptyShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		new SystemEnvironmentOrigin("");
	}

	@Test
	public void getPropertyShouldReturnProperty() {
		SystemEnvironmentOrigin origin = new SystemEnvironmentOrigin("FOO_BAR");
		assertThat(origin.getProperty()).isEqualTo("FOO_BAR");
	}

	@Test
	public void toStringShouldReturnStringWithDetails() {
		SystemEnvironmentOrigin origin = new SystemEnvironmentOrigin("FOO_BAR");
		assertThat(origin.toString())
				.isEqualTo("System Environment Property \"FOO_BAR\"");
	}

}
