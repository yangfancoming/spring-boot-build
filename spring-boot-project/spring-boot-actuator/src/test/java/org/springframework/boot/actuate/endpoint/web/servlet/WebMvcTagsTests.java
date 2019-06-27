

package org.springframework.boot.actuate.endpoint.web.servlet;

import io.micrometer.core.instrument.Tag;
import org.junit.Test;

import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTags;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebMvcTags}.
 *
 * @author Andy Wilkinson
 * @author Brian Clozel
 */
public class WebMvcTagsTests {

	private final MockHttpServletRequest request = new MockHttpServletRequest();

	private final MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	public void uriTagValueIsBestMatchingPatternWhenAvailable() {
		this.request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE,
				"/spring");
		this.response.setStatus(301);
		Tag tag = WebMvcTags.uri(this.request, this.response);
		assertThat(tag.getValue()).isEqualTo("/spring");
	}

	@Test
	public void uriTagValueIsRootWhenRequestHasNoPatternOrPathInfo() {
		assertThat(WebMvcTags.uri(this.request, null).getValue()).isEqualTo("root");
	}

	@Test
	public void uriTagValueIsRootWhenRequestHasNoPatternAndSlashPathInfo() {
		this.request.setPathInfo("/");
		assertThat(WebMvcTags.uri(this.request, null).getValue()).isEqualTo("root");
	}

	@Test
	public void uriTagValueIsUnknownWhenRequestHasNoPatternAndNonRootPathInfo() {
		this.request.setPathInfo("/example");
		assertThat(WebMvcTags.uri(this.request, null).getValue()).isEqualTo("UNKNOWN");
	}

	@Test
	public void uriTagValueIsRedirectionWhenResponseStatusIs3xx() {
		this.response.setStatus(301);
		Tag tag = WebMvcTags.uri(this.request, this.response);
		assertThat(tag.getValue()).isEqualTo("REDIRECTION");
	}

	@Test
	public void uriTagValueIsNotFoundWhenResponseStatusIs404() {
		this.response.setStatus(404);
		Tag tag = WebMvcTags.uri(this.request, this.response);
		assertThat(tag.getValue()).isEqualTo("NOT_FOUND");
	}

	@Test
	public void uriTagToleratesCustomResponseStatus() {
		this.response.setStatus(601);
		Tag tag = WebMvcTags.uri(this.request, this.response);
		assertThat(tag.getValue()).isEqualTo("root");
	}

	@Test
	public void uriTagIsUnknownWhenRequestIsNull() {
		Tag tag = WebMvcTags.uri(null, null);
		assertThat(tag.getValue()).isEqualTo("UNKNOWN");
	}

}
