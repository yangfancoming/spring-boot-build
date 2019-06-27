

package org.springframework.boot.autoconfigure.web.reactive.function.client;

import java.net.URI;

import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link WebClientAutoConfiguration}
 *
 * @author Brian Clozel
 */
public class WebClientAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class));

	@Test
	public void shouldCreateBuilder() {
		this.contextRunner.run((context) -> {
			WebClient.Builder builder = context.getBean(WebClient.Builder.class);
			WebClient webClient = builder.build();
			assertThat(webClient).isNotNull();
		});

	}

	@Test
	public void shouldCustomizeClientCodecs() {
		this.contextRunner.withUserConfiguration(CodecConfiguration.class)
				.run((context) -> {
					WebClient.Builder builder = context.getBean(WebClient.Builder.class);
					CodecCustomizer codecCustomizer = context
							.getBean(CodecCustomizer.class);
					WebClientCodecCustomizer clientCustomizer = context
							.getBean(WebClientCodecCustomizer.class);
					builder.build();
					assertThat(clientCustomizer).isNotNull();
					verify(codecCustomizer).customize(any(CodecConfigurer.class));
				});
	}

	@Test
	public void webClientShouldApplyCustomizers() {
		this.contextRunner.withUserConfiguration(WebClientCustomizerConfig.class)
				.run((context) -> {
					WebClient.Builder builder = context.getBean(WebClient.Builder.class);
					WebClientCustomizer customizer = context
							.getBean(WebClientCustomizer.class);
					builder.build();
					verify(customizer).customize(any(WebClient.Builder.class));
				});
	}

	@Test
	public void shouldGetPrototypeScopedBean() {
		this.contextRunner.withUserConfiguration(WebClientCustomizerConfig.class)
				.run((context) -> {
					ClientHttpResponse response = mock(ClientHttpResponse.class);
					ClientHttpConnector firstConnector = mock(ClientHttpConnector.class);
					given(firstConnector.connect(any(), any(), any()))
							.willReturn(Mono.just(response));
					WebClient.Builder firstBuilder = context
							.getBean(WebClient.Builder.class);
					firstBuilder.clientConnector(firstConnector)
							.baseUrl("http://first.example.org");
					ClientHttpConnector secondConnector = mock(ClientHttpConnector.class);
					given(secondConnector.connect(any(), any(), any()))
							.willReturn(Mono.just(response));
					WebClient.Builder secondBuilder = context
							.getBean(WebClient.Builder.class);
					secondBuilder.clientConnector(secondConnector)
							.baseUrl("http://second.example.org");
					assertThat(firstBuilder).isNotEqualTo(secondBuilder);
					firstBuilder.build().get().uri("/foo").exchange().block();
					secondBuilder.build().get().uri("/foo").exchange().block();
					verify(firstConnector).connect(eq(HttpMethod.GET),
							eq(URI.create("http://first.example.org/foo")), any());
					verify(secondConnector).connect(eq(HttpMethod.GET),
							eq(URI.create("http://second.example.org/foo")), any());
					WebClientCustomizer customizer = context
							.getBean(WebClientCustomizer.class);
					verify(customizer, times(1)).customize(any(WebClient.Builder.class));
				});
	}

	@Test
	public void shouldNotCreateClientBuilderIfAlreadyPresent() {
		this.contextRunner.withUserConfiguration(WebClientCustomizerConfig.class,
				CustomWebClientBuilderConfig.class).run((context) -> {
					WebClient.Builder builder = context.getBean(WebClient.Builder.class);
					assertThat(builder).isInstanceOf(MyWebClientBuilder.class);
				});
	}

	@Configuration
	static class CodecConfiguration {

		@Bean
		public CodecCustomizer myCodecCustomizer() {
			return mock(CodecCustomizer.class);
		}

	}

	@Configuration
	static class WebClientCustomizerConfig {

		@Bean
		public WebClientCustomizer webClientCustomizer() {
			return mock(WebClientCustomizer.class);
		}

	}

	@Configuration
	static class CustomWebClientBuilderConfig {

		@Bean
		public MyWebClientBuilder myWebClientBuilder() {
			return mock(MyWebClientBuilder.class);
		}

	}

	interface MyWebClientBuilder extends WebClient.Builder {

	}

}
