

package org.springframework.boot.autoconfigure.web.servlet;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DispatcherServletPath}.
 *
 * @author Phillip Webb
 */
public class DispatcherServletPathTests {

	@Test
	public void getRelativePathReturnsRelativePath() {
		assertThat(((DispatcherServletPath) () -> "spring").getRelativePath("boot"))
				.isEqualTo("spring/boot");
		assertThat(((DispatcherServletPath) () -> "spring/").getRelativePath("boot"))
				.isEqualTo("spring/boot");
		assertThat(((DispatcherServletPath) () -> "spring").getRelativePath("/boot"))
				.isEqualTo("spring/boot");
	}

	@Test
	public void getPrefixWhenHasSimplePathReturnPath() {
		assertThat(((DispatcherServletPath) () -> "spring").getPrefix())
				.isEqualTo("spring");
	}

	@Test
	public void getPrefixWhenHasPatternRemovesPattern() {
		assertThat(((DispatcherServletPath) () -> "spring/*.do").getPrefix())
				.isEqualTo("spring");
	}

	@Test
	public void getPathWhenPathEndsWithSlashRemovesSlash() {
		assertThat(((DispatcherServletPath) () -> "spring/").getPrefix())
				.isEqualTo("spring");
	}

	@Test
	public void getServletUrlMappingWhenPathIsEmptyReturnsSlash() {
		assertThat(((DispatcherServletPath) () -> "").getServletUrlMapping())
				.isEqualTo("/");
	}

	@Test
	public void getServletUrlMappingWhenPathIsSlashReturnsSlash() {
		assertThat(((DispatcherServletPath) () -> "/").getServletUrlMapping())
				.isEqualTo("/");
	}

	@Test
	public void getServletUrlMappingWhenPathContainsStarReturnsPath() {
		assertThat(((DispatcherServletPath) () -> "spring/*.do").getServletUrlMapping())
				.isEqualTo("spring/*.do");
	}

	@Test
	public void getServletUrlMappingWhenHasPathNotEndingSlashReturnsSlashStarPattern() {
		assertThat(((DispatcherServletPath) () -> "spring/boot").getServletUrlMapping())
				.isEqualTo("spring/boot/*");
	}

	@Test
	public void getServletUrlMappingWhenHasPathEndingWithSlashReturnsSlashStarPattern() {
		assertThat(((DispatcherServletPath) () -> "spring/boot/").getServletUrlMapping())
				.isEqualTo("spring/boot/*");
	}

}
