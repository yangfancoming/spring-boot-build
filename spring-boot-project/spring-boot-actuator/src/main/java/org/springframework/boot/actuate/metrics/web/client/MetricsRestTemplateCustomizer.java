

package org.springframework.boot.actuate.metrics.web.client;

import java.util.ArrayList;
import java.util.List;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

/**
 * {@link RestTemplateCustomizer} that configures the {@link RestTemplate} to record
 * request metrics.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
public class MetricsRestTemplateCustomizer implements RestTemplateCustomizer {

	private final MetricsClientHttpRequestInterceptor interceptor;

	/**
	 * Creates a new {@code MetricsRestTemplateInterceptor} that will record metrics using
	 * the given {@code meterRegistry} with tags provided by the given
	 * {@code tagProvider}.
	 * @param meterRegistry the meter registry
	 * @param tagProvider the tag provider
	 * @param metricName the name of the recorded metric
	 */
	public MetricsRestTemplateCustomizer(MeterRegistry meterRegistry,
			RestTemplateExchangeTagsProvider tagProvider, String metricName) {
		this.interceptor = new MetricsClientHttpRequestInterceptor(meterRegistry,
				tagProvider, metricName);
	}

	@Override
	public void customize(RestTemplate restTemplate) {
		UriTemplateHandler templateHandler = restTemplate.getUriTemplateHandler();
		templateHandler = this.interceptor.createUriTemplateHandler(templateHandler);
		restTemplate.setUriTemplateHandler(templateHandler);
		List<ClientHttpRequestInterceptor> existingInterceptors = restTemplate
				.getInterceptors();
		if (!existingInterceptors.contains(this.interceptor)) {
			List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
			interceptors.add(this.interceptor);
			interceptors.addAll(existingInterceptors);
			restTemplate.setInterceptors(interceptors);
		}
	}

}
