

package org.springframework.boot.autoconfigure.web.reactive.function.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link WebClient}.
 * <p>
 * This will produce a
 * {@link org.springframework.web.reactive.function.client.WebClient.Builder
 * WebClient.Builder} bean with the {@code prototype} scope, meaning each injection point
 * will receive a newly cloned instance of the builder.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(WebClient.class)
@AutoConfigureAfter(CodecsAutoConfiguration.class)
public class WebClientAutoConfiguration {

	private final WebClient.Builder webClientBuilder;

	public WebClientAutoConfiguration(
			ObjectProvider<List<WebClientCustomizer>> customizerProvider) {
		this.webClientBuilder = WebClient.builder();
		List<WebClientCustomizer> customizers = customizerProvider.getIfAvailable();
		if (!CollectionUtils.isEmpty(customizers)) {
			customizers = new ArrayList<>(customizers);
			AnnotationAwareOrderComparator.sort(customizers);
			customizers
					.forEach((customizer) -> customizer.customize(this.webClientBuilder));
		}
	}

	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	public WebClient.Builder webClientBuilder() {
		return this.webClientBuilder.clone();
	}

	@Configuration
	@ConditionalOnBean(CodecCustomizer.class)
	protected static class WebClientCodecsConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@Order(0)
		public WebClientCodecCustomizer exchangeStrategiesCustomizer(
				List<CodecCustomizer> codecCustomizers) {
			return new WebClientCodecCustomizer(codecCustomizers);
		}

	}

}