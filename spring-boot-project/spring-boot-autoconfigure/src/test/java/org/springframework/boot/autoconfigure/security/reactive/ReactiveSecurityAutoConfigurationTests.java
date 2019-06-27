

package org.springframework.boot.autoconfigure.security.reactive;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.security.web.server.WebFilterChainProxy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReactiveSecurityAutoConfiguration}.
 *
 * @author Madhura Bhave
 */
public class ReactiveSecurityAutoConfigurationTests {

	private ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner();

	@Test
	public void importsConfigurationThatEnablesWebFluxSecurity() {
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

}
