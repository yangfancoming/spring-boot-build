

package org.springframework.boot.actuate.metrics.web.reactive.server;

import io.micrometer.core.instrument.Tag;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WebFluxTags}.
 *
 * @author Brian Clozel
 */
public class WebFluxTagsTests {

	private MockServerWebExchange exchange;

	private PathPatternParser parser = new PathPatternParser();

	@Before
	public void setup() {
		this.exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
	}

	@Test
	public void uriTagValueIsBestMatchingPatternWhenAvailable() {
		this.exchange.getAttributes().put(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE,
				this.parser.parse("/spring"));
		this.exchange.getResponse().setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		Tag tag = WebFluxTags.uri(this.exchange);
		assertThat(tag.getValue()).isEqualTo("/spring");
	}

	@Test
	public void uriTagValueIsRedirectionWhenResponseStatusIs3xx() {
		this.exchange.getResponse().setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		Tag tag = WebFluxTags.uri(this.exchange);
		assertThat(tag.getValue()).isEqualTo("REDIRECTION");
	}

	@Test
	public void uriTagValueIsNotFoundWhenResponseStatusIs404() {
		this.exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
		Tag tag = WebFluxTags.uri(this.exchange);
		assertThat(tag.getValue()).isEqualTo("NOT_FOUND");
	}

	@Test
	public void uriTagToleratesCustomResponseStatus() {
		this.exchange.getResponse().setStatusCodeValue(601);
		Tag tag = WebFluxTags.uri(this.exchange);
		assertThat(tag.getValue()).isEqualTo("root");
	}

	@Test
	public void uriTagIsUnknownWhenRequestIsNull() {
		Tag tag = WebFluxTags.uri(null);
		assertThat(tag.getValue()).isEqualTo("UNKNOWN");
	}

	@Test
	public void methodTagToleratesNonStandardHttpMethods() {
		ServerWebExchange exchange = mock(ServerWebExchange.class);
		ServerHttpRequest request = mock(ServerHttpRequest.class);
		given(exchange.getRequest()).willReturn(request);
		given(request.getMethodValue()).willReturn("CUSTOM");
		Tag tag = WebFluxTags.method(exchange);
		assertThat(tag.getValue()).isEqualTo("CUSTOM");
	}

}
