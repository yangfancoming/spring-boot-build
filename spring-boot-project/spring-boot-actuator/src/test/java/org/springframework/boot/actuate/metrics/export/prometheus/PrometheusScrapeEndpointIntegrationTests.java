

package org.springframework.boot.actuate.metrics.export.prometheus;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.actuate.endpoint.web.test.WebEndpointRunners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Tests for {@link PrometheusScrapeEndpoint}.
 *
 * @author Jon Schneider
 */
@RunWith(WebEndpointRunners.class)
public class PrometheusScrapeEndpointIntegrationTests {

	private static WebTestClient client;

	@Test
	public void scrapeHasContentTypeText004() {
		client.get().uri("/actuator/prometheus").exchange().expectStatus().isOk()
				.expectHeader()
				.contentType(MediaType.parseMediaType(TextFormat.CONTENT_TYPE_004));
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public PrometheusScrapeEndpoint prometheusScrapeEndpoint(
				CollectorRegistry collectorRegistry) {
			return new PrometheusScrapeEndpoint(collectorRegistry);
		}

		@Bean
		public CollectorRegistry collectorRegistry() {
			return new CollectorRegistry(true);
		}

		@Bean
		public MeterRegistry registry(CollectorRegistry registry) {
			return new PrometheusMeterRegistry((k) -> null, registry, Clock.SYSTEM);
		}

	}

}
