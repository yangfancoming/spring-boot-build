

package org.springframework.boot.actuate.endpoint.web;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EndpointMapping}.
 *
 * @author Andy Wilkinson
 */
public class EndpointMappingTests {

	@Test
	public void normalizationTurnsASlashIntoAnEmptyString() {
		assertThat(new EndpointMapping("/").getPath()).isEqualTo("");
	}

	@Test
	public void normalizationLeavesAnEmptyStringAsIs() {
		assertThat(new EndpointMapping("").getPath()).isEqualTo("");
	}

	@Test
	public void normalizationRemovesATrailingSlash() {
		assertThat(new EndpointMapping("/test/").getPath()).isEqualTo("/test");
	}

	@Test
	public void normalizationAddsALeadingSlash() {
		assertThat(new EndpointMapping("test").getPath()).isEqualTo("/test");
	}

	@Test
	public void normalizationAddsALeadingSlashAndRemovesATrailingSlash() {
		assertThat(new EndpointMapping("test/").getPath()).isEqualTo("/test");
	}

	@Test
	public void normalizationLeavesAPathWithALeadingSlashAndNoTrailingSlashAsIs() {
		assertThat(new EndpointMapping("/test").getPath()).isEqualTo("/test");
	}

	@Test
	public void subPathForAnEmptyStringReturnsBasePath() {
		assertThat(new EndpointMapping("/test").createSubPath("")).isEqualTo("/test");
	}

	@Test
	public void subPathWithALeadingSlashIsSeparatedFromBasePathBySingleSlash() {
		assertThat(new EndpointMapping("/test").createSubPath("/one"))
				.isEqualTo("/test/one");
	}

	@Test
	public void subPathWithoutALeadingSlashIsSeparatedFromBasePathBySingleSlash() {
		assertThat(new EndpointMapping("/test").createSubPath("one"))
				.isEqualTo("/test/one");
	}

	@Test
	public void trailingSlashIsRemovedFromASubPath() {
		assertThat(new EndpointMapping("/test").createSubPath("one/"))
				.isEqualTo("/test/one");
	}

}
