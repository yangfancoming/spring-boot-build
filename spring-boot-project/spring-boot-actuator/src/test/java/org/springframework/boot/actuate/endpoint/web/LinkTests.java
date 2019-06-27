

package org.springframework.boot.actuate.endpoint.web;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Link}.
 *
 * @author Phillip Webb
 */
public class LinkTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenHrefIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("HREF must not be null");
		new Link(null);
	}

	@Test
	public void getHrefShouldReturnHref() {
		String href = "http://example.com";
		Link link = new Link(href);
		assertThat(link.getHref()).isEqualTo(href);
	}

	@Test
	public void isTemplatedWhenContainsPlaceholderShouldReturnTrue() {
		String href = "http://example.com/{path}";
		Link link = new Link(href);
		assertThat(link.isTemplated()).isTrue();
	}

	@Test
	public void isTemplatedWhenContainsNoPlaceholderShouldReturnFalse() {
		String href = "http://example.com/path";
		Link link = new Link(href);
		assertThat(link.isTemplated()).isFalse();
	}

}
