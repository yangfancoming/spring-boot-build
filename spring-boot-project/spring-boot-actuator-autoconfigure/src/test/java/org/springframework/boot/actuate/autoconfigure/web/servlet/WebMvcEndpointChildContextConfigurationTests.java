

package org.springframework.boot.actuate.autoconfigure.web.servlet;

import org.junit.Test;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebMvcEndpointChildContextConfiguration}.
 *
 * @author Madhura Bhave
 */
public class WebMvcEndpointChildContextConfigurationTests {

	private WebApplicationContextRunner contextRunner = new WebApplicationContextRunner();

	@Test
	public void contextShouldConfigureRequestContextFilter() {
		this.contextRunner
				.withUserConfiguration(WebMvcEndpointChildContextConfiguration.class)
				.run((context) -> assertThat(context)
						.hasSingleBean(OrderedRequestContextFilter.class));
	}

	@Test
	public void contextShouldNotConfigureRequestContextFilterWhenPresent() {
		this.contextRunner.withUserConfiguration(ExistingConfig.class,
				WebMvcEndpointChildContextConfiguration.class).run((context) -> {
					assertThat(context).hasSingleBean(RequestContextFilter.class);
					assertThat(context).hasBean("testRequestContextFilter");
				});
	}

	@Test
	public void contextShouldNotConfigureRequestContextFilterWhenRequestContextListenerPresent() {
		this.contextRunner.withUserConfiguration(RequestContextListenerConfig.class,
				WebMvcEndpointChildContextConfiguration.class).run((context) -> {
					assertThat(context).hasSingleBean(RequestContextListener.class);
					assertThat(context)
							.doesNotHaveBean(OrderedRequestContextFilter.class);
				});
	}

	@Test
	public void contextShouldConfigureDispatcherServletPathWithRootPath() {
		this.contextRunner
				.withUserConfiguration(WebMvcEndpointChildContextConfiguration.class)
				.run((context) -> assertThat(
						context.getBean(DispatcherServletPath.class).getPath())
								.isEqualTo("/"));
	}

	static class ExistingConfig {

		@Bean
		public RequestContextFilter testRequestContextFilter() {
			return new RequestContextFilter();
		}

	}

	static class RequestContextListenerConfig {

		@Bean
		public RequestContextListener testRequestContextListener() {
			return new RequestContextListener();
		}

	}

}
