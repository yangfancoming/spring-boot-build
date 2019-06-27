

package org.springframework.boot.autoconfigure.h2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link H2ConsoleProperties}.
 *
 * @author Madhura Bhave
 */
public class H2ConsolePropertiesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private H2ConsoleProperties properties;

	@Test
	public void pathMustNotBeEmpty() {
		this.properties = new H2ConsoleProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must have length greater than 1");
		this.properties.setPath("");
	}

	@Test
	public void pathMustHaveLengthGreaterThanOne() {
		this.properties = new H2ConsoleProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must have length greater than 1");
		this.properties.setPath("/");
	}

	@Test
	public void customPathMustBeginWithASlash() {
		this.properties = new H2ConsoleProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must start with '/'");
		this.properties.setPath("custom");
	}

}
