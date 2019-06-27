

package org.springframework.boot.autoconfigure.web.reactive;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Tests for {@link HttpHandlerAutoConfiguration}.
 *
 * @author Brian Clozel
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
public class HttpHandlerAutoConfigurationTests {

	private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(HttpHandlerAutoConfiguration.class));

	@Test
	public void shouldNotProcessIfExistingHttpHandler() {
		this.contextRunner.withUserConfiguration(CustomHttpHandler.class)
				.run((context) -> {
					assertThat(context).hasSingleBean(HttpHandler.class);
					assertThat(context).getBean(HttpHandler.class)
							.isSameAs(context.getBean("customHttpHandler"));
				});
	}

	@Test
	public void shouldConfigureHttpHandlerAnnotation() {
		this.contextRunner
				.withConfiguration(AutoConfigurations.of(WebFluxAutoConfiguration.class))
				.run((context) -> assertThat(context).hasSingleBean(HttpHandler.class));
	}

	@Configuration
	protected static class CustomHttpHandler {

		@Bean
		public HttpHandler customHttpHandler() {
			return (serverHttpRequest, serverHttpResponse) -> null;
		}

		@Bean
		public RouterFunction<ServerResponse> routerFunction() {
			return route(GET("/test"), (serverRequest) -> null);
		}

	}

}
