

package org.springframework.boot.autoconfigure.web.reactive.function.client;

import java.util.List;

import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * {@link WebClientCustomizer} that configures codecs for the HTTP client.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
public class WebClientCodecCustomizer implements WebClientCustomizer {

	private final List<CodecCustomizer> codecCustomizers;

	public WebClientCodecCustomizer(List<CodecCustomizer> codecCustomizers) {
		this.codecCustomizers = codecCustomizers;
	}

	@Override
	public void customize(WebClient.Builder webClientBuilder) {
		webClientBuilder
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs((codecs) -> this.codecCustomizers
								.forEach((customizer) -> customizer.customize(codecs)))
						.build());
	}

}
