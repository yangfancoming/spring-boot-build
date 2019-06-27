

package org.springframework.boot.autoconfigure.security.reactive;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.WebFilterChainProxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WebFluxSecurityConfiguration}.
 *
 * @author Madhura Bhave
 */
public class WebFluxSecurityConfigurationTests {

	private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner();

	@Test
	public void backsOffWhenWebFilterChainProxyBeanPresent() {
		this.contextRunner
				.withConfiguration(
						AutoConfigurations.of(ReactiveSecurityAutoConfiguration.class))
				.withUserConfiguration(WebFilterChainProxyConfiguration.class)
				.run((context) -> assertThat(context)
						.doesNotHaveBean(WebFluxSecurityConfiguration.class));
	}

	@Test
	public void enablesWebFluxSecurity() {
		this.contextRunner
				.withConfiguration(
						AutoConfigurations.of(ReactiveSecurityAutoConfiguration.class,
								ReactiveUserDetailsServiceAutoConfiguration.class))
				.run((context) -> {
					assertThat(context).getBean(WebFilterChainProxy.class).isNotNull();
					assertThat(context).getBean(WebFluxSecurityConfiguration.class)
							.isNotNull();
					assertThat(context).getBean(WebFilterChainProxy.class).isNotNull();
				});
	}

	@Configuration
	static class WebFilterChainProxyConfiguration {

		@Bean
		public WebFilterChainProxy webFilterChainProxy() {
			return mock(WebFilterChainProxy.class);
		}

	}

}
