

package org.springframework.boot.web.reactive.context;

import org.junit.Test;

import org.springframework.boot.web.reactive.context.config.ExampleReactiveWebServerApplicationConfiguration;
import org.springframework.boot.web.reactive.server.MockReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link AnnotationConfigReactiveWebServerApplicationContext}.
 *
 * @author Phillip Webb
 */
public class AnnotationConfigReactiveWebServerApplicationContextTests {

	private AnnotationConfigReactiveWebServerApplicationContext context;

	@Test
	public void createFromScan() {
		this.context = new AnnotationConfigReactiveWebServerApplicationContext(
				ExampleReactiveWebServerApplicationConfiguration.class.getPackage()
						.getName());
		verifyContext();
	}

	@Test
	public void createFromConfigClass() {
		this.context = new AnnotationConfigReactiveWebServerApplicationContext(
				ExampleReactiveWebServerApplicationConfiguration.class);
		verifyContext();
	}

	@Test
	public void registerAndRefresh() {
		this.context = new AnnotationConfigReactiveWebServerApplicationContext();
		this.context.register(ExampleReactiveWebServerApplicationConfiguration.class);
		this.context.refresh();
		verifyContext();
	}

	@Test
	public void multipleRegistersAndRefresh() {
		this.context = new AnnotationConfigReactiveWebServerApplicationContext();
		this.context.register(WebServerConfiguration.class);
		this.context.register(HttpHandlerConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(WebServerConfiguration.class)).hasSize(1);
		assertThat(this.context.getBeansOfType(HttpHandlerConfiguration.class))
				.hasSize(1);
	}

	@Test
	public void scanAndRefresh() {
		this.context = new AnnotationConfigReactiveWebServerApplicationContext();
		this.context.scan(ExampleReactiveWebServerApplicationConfiguration.class
				.getPackage().getName());
		this.context.refresh();
		verifyContext();
	}

	private void verifyContext() {
		MockReactiveWebServerFactory factory = this.context
				.getBean(MockReactiveWebServerFactory.class);
		HttpHandler httpHandler = this.context.getBean(HttpHandler.class);
		assertThat(factory.getWebServer().getHttpHandler()).isEqualTo(httpHandler);
	}

	@Configuration
	public static class WebServerConfiguration {

		@Bean
		public ReactiveWebServerFactory webServerFactory() {
			return new MockReactiveWebServerFactory();
		}

	}

	@Configuration
	public static class HttpHandlerConfiguration {

		@Bean
		public HttpHandler httpHandler() {
			return mock(HttpHandler.class);
		}

	}

}
