

package org.springframework.boot.autoconfigure.web.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link RestTemplate}.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 1.4.0
 */
@Configuration
@AutoConfigureAfter(HttpMessageConvertersAutoConfiguration.class)
@ConditionalOnClass(RestTemplate.class)
public class RestTemplateAutoConfiguration {

	private final ObjectProvider<HttpMessageConverters> messageConverters;

	private final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers;

	public RestTemplateAutoConfiguration(
			ObjectProvider<HttpMessageConverters> messageConverters,
			ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
		this.messageConverters = messageConverters;
		this.restTemplateCustomizers = restTemplateCustomizers;
	}

	@Bean
	@ConditionalOnMissingBean
	public RestTemplateBuilder restTemplateBuilder() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		HttpMessageConverters converters = this.messageConverters.getIfUnique();
		if (converters != null) {
			builder = builder.messageConverters(converters.getConverters());
		}
		List<RestTemplateCustomizer> customizers = this.restTemplateCustomizers
				.getIfAvailable();
		if (!CollectionUtils.isEmpty(customizers)) {
			customizers = new ArrayList<>(customizers);
			AnnotationAwareOrderComparator.sort(customizers);
			builder = builder.customizers(customizers);
		}
		return builder;
	}

}
