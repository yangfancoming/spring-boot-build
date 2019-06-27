

package org.springframework.boot.actuate.metrics.web.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.StreamSupport;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MockClock;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MetricsRestTemplateCustomizer}.
 *
 * @author Jon Schneider
 * @author Brian Clozel
 */
public class MetricsRestTemplateCustomizerTests {

	private MeterRegistry registry;

	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private MetricsRestTemplateCustomizer customizer;

	@Before
	public void setup() {
		this.registry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, new MockClock());
		this.restTemplate = new RestTemplate();
		this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
		this.customizer = new MetricsRestTemplateCustomizer(this.registry,
				new DefaultRestTemplateExchangeTagsProvider(), "http.client.requests");
		this.customizer.customize(this.restTemplate);
	}

	@Test
	public void interceptRestTemplate() {
		this.mockServer.expect(MockRestRequestMatchers.requestTo("/test/123"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess("OK",
						MediaType.APPLICATION_JSON));
		String result = this.restTemplate.getForObject("/test/{id}", String.class, 123);
		assertThat(this.registry.find("http.client.requests").meters())
				.anySatisfy((m) -> assertThat(
						StreamSupport.stream(m.getId().getTags().spliterator(), false)
								.map(Tag::getKey)).doesNotContain("bucket"));
		assertThat(this.registry.get("http.client.requests")
				.tags("method", "GET", "uri", "/test/{id}", "status", "200").timer()
				.count()).isEqualTo(1);
		assertThat(result).isEqualTo("OK");
		this.mockServer.verify();
	}

	@Test
	public void avoidDuplicateRegistration() {
		this.customizer.customize(this.restTemplate);
		assertThat(this.restTemplate.getInterceptors()).hasSize(1);
		this.customizer.customize(this.restTemplate);
		assertThat(this.restTemplate.getInterceptors()).hasSize(1);
	}

	@Test
	public void normalizeUriToContainLeadingSlash() {
		this.mockServer.expect(MockRestRequestMatchers.requestTo("/test/123"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess("OK",
						MediaType.APPLICATION_JSON));
		String result = this.restTemplate.getForObject("test/{id}", String.class, 123);
		this.registry.get("http.client.requests").tags("uri", "/test/{id}").timer();
		assertThat(result).isEqualTo("OK");
		this.mockServer.verify();
	}

	@Test
	public void interceptRestTemplateWithUri() throws URISyntaxException {
		this.mockServer
				.expect(MockRestRequestMatchers.requestTo("http://localhost/test/123"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess("OK",
						MediaType.APPLICATION_JSON));
		String result = this.restTemplate
				.getForObject(new URI("http://localhost/test/123"), String.class);
		assertThat(result).isEqualTo("OK");
		this.registry.get("http.client.requests").tags("uri", "/test/123").timer();
		this.mockServer.verify();
	}

}
