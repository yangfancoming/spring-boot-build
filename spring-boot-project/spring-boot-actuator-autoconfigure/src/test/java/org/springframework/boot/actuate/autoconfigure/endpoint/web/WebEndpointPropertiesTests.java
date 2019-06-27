

package org.springframework.boot.actuate.autoconfigure.endpoint.web;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebEndpointProperties}.
 *
 * @author Madhura Bhave
 */
public class WebEndpointPropertiesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void defaultBasePathShouldBeApplication() {
		WebEndpointProperties properties = new WebEndpointProperties();
		assertThat(properties.getBasePath()).isEqualTo("/actuator");
	}

	@Test
	public void basePathShouldBeCleaned() {
		WebEndpointProperties properties = new WebEndpointProperties();
		properties.setBasePath("/");
		assertThat(properties.getBasePath()).isEqualTo("");
		properties.setBasePath("/actuator/");
		assertThat(properties.getBasePath()).isEqualTo("/actuator");
	}

	@Test
	public void basePathMustStartWithSlash() {
		WebEndpointProperties properties = new WebEndpointProperties();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Base path must start with '/' or be empty");
		properties.setBasePath("admin");
	}

	@Test
	public void basePathCanBeEmpty() {
		WebEndpointProperties properties = new WebEndpointProperties();
		properties.setBasePath("");
		assertThat(properties.getBasePath()).isEqualTo("");
	}

}
