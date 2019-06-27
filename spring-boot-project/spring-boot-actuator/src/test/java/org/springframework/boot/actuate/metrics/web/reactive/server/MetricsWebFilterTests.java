

package org.springframework.boot.actuate.metrics.web.reactive.server;

import io.micrometer.core.instrument.MockClock;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MetricsWebFilter}
 *
 * @author Brian Clozel
 */
public class MetricsWebFilterTests {

	private static final String REQUEST_METRICS_NAME = "http.server.requests";

	private SimpleMeterRegistry registry;

	private MetricsWebFilter webFilter;

	@Before
	public void setup() {
		MockClock clock = new MockClock();
		this.registry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, clock);
		this.webFilter = new MetricsWebFilter(this.registry,
				new DefaultWebFluxTagsProvider(), REQUEST_METRICS_NAME);
	}

	@Test
	public void filterAddsTagsToRegistry() {
		MockServerWebExchange exchange = createExchange("/projects/spring-boot",
				"/projects/{project}");
		this.webFilter
				.filter(exchange,
						(serverWebExchange) -> exchange.getResponse().setComplete())
				.block();
		assertMetricsContainsTag("uri", "/projects/{project}");
		assertMetricsContainsTag("status", "200");
	}

	@Test
	public void filterAddsTagsToRegistryForExceptions() {
		MockServerWebExchange exchange = createExchange("/projects/spring-boot",
				"/projects/{project}");
		this.webFilter
				.filter(exchange,
						(serverWebExchange) -> Mono
								.error(new IllegalStateException("test error")))
				.onErrorResume((t) -> {
					exchange.getResponse().setStatusCodeValue(500);
					return exchange.getResponse().setComplete();
				}).block();
		assertMetricsContainsTag("uri", "/projects/{project}");
		assertMetricsContainsTag("status", "500");
		assertMetricsContainsTag("exception", "IllegalStateException");
	}

	@Test
	public void filterAddsNonEmptyTagsToRegistryForAnonymousExceptions() {
		final Exception anonymous = new Exception("test error") {
		};

		MockServerWebExchange exchange = createExchange("/projects/spring-boot",
				"/projects/{project}");
		this.webFilter.filter(exchange, (serverWebExchange) -> Mono.error(anonymous))
				.onErrorResume((t) -> {
					exchange.getResponse().setStatusCodeValue(500);
					return exchange.getResponse().setComplete();
				}).block();
		assertMetricsContainsTag("uri", "/projects/{project}");
		assertMetricsContainsTag("status", "500");
		assertMetricsContainsTag("exception", anonymous.getClass().getName());
	}

	@Test
	public void filterAddsTagsToRegistryForExceptionsAndCommittedResponse() {
		MockServerWebExchange exchange = createExchange("/projects/spring-boot",
				"/projects/{project}");
		this.webFilter.filter(exchange, (serverWebExchange) -> {
			exchange.getResponse().setStatusCodeValue(500);
			return exchange.getResponse().setComplete()
					.then(Mono.error(new IllegalStateException("test error")));
		}).onErrorResume((t) -> Mono.empty()).block();
		assertMetricsContainsTag("uri", "/projects/{project}");
		assertMetricsContainsTag("status", "500");
	}

	private MockServerWebExchange createExchange(String path, String pathPattern) {
		PathPatternParser parser = new PathPatternParser();
		MockServerWebExchange exchange = MockServerWebExchange
				.from(MockServerHttpRequest.get(path).build());
		exchange.getAttributes().put(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE,
				parser.parse(pathPattern));
		return exchange;
	}

	private void assertMetricsContainsTag(String tagKey, String tagValue) {
		assertThat(this.registry.get(REQUEST_METRICS_NAME).tag(tagKey, tagValue).timer()
				.count()).isEqualTo(1);
	}

}
