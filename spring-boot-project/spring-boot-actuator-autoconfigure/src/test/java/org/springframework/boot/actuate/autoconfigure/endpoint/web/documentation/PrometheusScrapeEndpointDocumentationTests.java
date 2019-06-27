

package org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.junit.Test;

import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for generating documentation describing the {@link PrometheusScrapeEndpoint}.
 *
 * @author Andy Wilkinson
 */
public class PrometheusScrapeEndpointDocumentationTests
		extends MockMvcEndpointDocumentationTests {

	@Test
	public void prometheus() throws Exception {
		this.mockMvc.perform(get("/actuator/prometheus")).andExpect(status().isOk())
				.andDo(document("prometheus"));
	}

	@Configuration
	@Import(BaseDocumentationConfiguration.class)
	static class TestConfiguration {

		@Bean
		public PrometheusScrapeEndpoint endpoint() {
			CollectorRegistry collectorRegistry = new CollectorRegistry(true);
			PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(
					new PrometheusConfig() {

						@Override
						public String get(String key) {
							return null;
						}

					}, collectorRegistry, Clock.SYSTEM);
			new JvmMemoryMetrics().bindTo(meterRegistry);
			return new PrometheusScrapeEndpoint(collectorRegistry);
		}

	}

}
