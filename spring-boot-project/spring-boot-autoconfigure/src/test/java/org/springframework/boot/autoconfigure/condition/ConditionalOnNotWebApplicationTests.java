

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.boot.autoconfigure.web.reactive.MockReactiveWebServerFactory;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link ConditionalOnNotWebApplication}.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
public class ConditionalOnNotWebApplicationTests {

	@Test
	public void testNotWebApplicationWithServletContext() {
		new WebApplicationContextRunner()
				.withUserConfiguration(NotWebApplicationConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean(String.class));
	}

	@Test
	public void testNotWebApplicationWithReactiveContext() {
		new ReactiveWebApplicationContextRunner()
				.withUserConfiguration(ReactiveApplicationConfig.class,
						NotWebApplicationConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean(String.class));
	}

	@Test
	public void testNotWebApplication() {
		new ApplicationContextRunner()
				.withUserConfiguration(NotWebApplicationConfiguration.class)
				.run((context) -> assertThat(context).getBeans(String.class)
						.containsExactly(entry("none", "none")));
	}

	@Configuration
	protected static class ReactiveApplicationConfig {

		@Bean
		public ReactiveWebServerFactory reactiveWebServerFactory() {
			return new MockReactiveWebServerFactory();
		}

		@Bean
		public HttpHandler httpHandler() {
			return (request, response) -> Mono.empty();
		}

	}

	@Configuration
	@ConditionalOnNotWebApplication
	protected static class NotWebApplicationConfiguration {

		@Bean
		public String none() {
			return "none";
		}

	}

}
