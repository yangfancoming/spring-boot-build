

package org.springframework.boot.autoconfigure.webservices;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link WebServicesProperties}.
 *
 * @author Madhura Bhave
 */
public class WebServicesPropertiesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private WebServicesProperties properties;

	@Test
	public void pathMustNotBeEmpty() {
		this.properties = new WebServicesProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must have length greater than 1");
		this.properties.setPath("");
	}

	@Test
	public void pathMustHaveLengthGreaterThanOne() {
		this.properties = new WebServicesProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must have length greater than 1");
		this.properties.setPath("/");
	}

	@Test
	public void customPathMustBeginWithASlash() {
		this.properties = new WebServicesProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must start with '/'");
		this.properties.setPath("custom");
	}

}
