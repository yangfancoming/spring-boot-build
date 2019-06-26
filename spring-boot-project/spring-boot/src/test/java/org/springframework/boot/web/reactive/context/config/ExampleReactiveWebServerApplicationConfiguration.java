

package org.springframework.boot.web.reactive.context.config;

import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContextTests;
import org.springframework.boot.web.reactive.server.MockReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import static org.mockito.Mockito.mock;

/**
 * Example {@code @Configuration} for use with
 * {@link AnnotationConfigReactiveWebServerApplicationContextTests}.
 *
 * @author Phillip Webb
 */
@Configuration
public class ExampleReactiveWebServerApplicationConfiguration {

	@Bean
	public MockReactiveWebServerFactory webServerFactory() {
		return new MockReactiveWebServerFactory();
	}

	@Bean
	public HttpHandler httpHandler() {
		return mock(HttpHandler.class);
	}

}
