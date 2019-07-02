

package org.springframework.boot.test.autoconfigure.web.reactive;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Consumer;

import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * {@link WebTestClientBuilderCustomizer} for a typical Spring Boot application. Usually
 * applied automatically via
 * {@link AutoConfigureWebTestClient @AutoConfigureWebTestClient}, but may also be used
 * directly.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class SpringBootWebTestClientBuilderCustomizer
		implements WebTestClientBuilderCustomizer {

	private final Collection<CodecCustomizer> codecCustomizers;

	private Duration timeout;

	/**
	 * Create a new {@code SpringBootWebTestClientBuilderCustomizer} that will configure
	 * the builder's codecs using the given {@code codecCustomizers}.
	 * @param codecCustomizers the codec customizers
	 */
	public SpringBootWebTestClientBuilderCustomizer(
			Collection<CodecCustomizer> codecCustomizers) {
		this.codecCustomizers = codecCustomizers;
	}

	public void setTimeout(Duration timeout) {
		this.timeout = timeout;
	}

	@Override
	public void customize(Builder builder) {
		if (this.timeout != null) {
			builder.responseTimeout(this.timeout);
		}
		customizeWebTestClientCodecs(builder);
	}

	private void customizeWebTestClientCodecs(WebTestClient.Builder builder) {
		if (!CollectionUtils.isEmpty(this.codecCustomizers)) {
			builder.exchangeStrategies(ExchangeStrategies.builder()
					.codecs(applyCustomizers(this.codecCustomizers)).build());
		}
	}

	private Consumer<ClientCodecConfigurer> applyCustomizers(
			Collection<CodecCustomizer> customizers) {
		return (codecs) -> customizers
				.forEach((customizer) -> customizer.customize(codecs));
	}

}
