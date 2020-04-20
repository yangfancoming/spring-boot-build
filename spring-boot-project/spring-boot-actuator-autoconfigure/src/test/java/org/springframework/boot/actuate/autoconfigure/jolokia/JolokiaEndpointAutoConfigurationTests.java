

package org.springframework.boot.actuate.autoconfigure.jolokia;

import java.util.Collection;
import java.util.Collections;

import org.jolokia.http.AgentServlet;
import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.ServletEndpointManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.servlet.ServletManagementContextAutoConfiguration;
import org.springframework.boot.actuate.endpoint.web.ExposableServletEndpoint;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.context.assertj.AssertableWebApplicationContext;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JolokiaEndpointAutoConfiguration}.
 *
 * @author Christian Dupuis
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class JolokiaEndpointAutoConfigurationTests {

	private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(
					DispatcherServletAutoConfiguration.class,
					ManagementContextAutoConfiguration.class,
					ServletManagementContextAutoConfiguration.class,
					ServletEndpointManagementContextConfiguration.class,
					JolokiaEndpointAutoConfiguration.class, TestConfiguration.class));

	@Test
	public void jolokiaServletShouldBeEnabledByDefault() {
		this.contextRunner.run((context) -> {
			ExposableServletEndpoint endpoint = getEndpoint(context);
			assertThat(endpoint.getRootPath()).isEqualTo("jolokia");
			Object servlet = ReflectionTestUtils.getField(endpoint.getEndpointServlet(),
					"servlet");
			assertThat(servlet).isInstanceOf(AgentServlet.class);
		});
	}

	@Test
	public void jolokiaServletWhenDisabledShouldNotBeDiscovered() {
		this.contextRunner.withPropertyValues("management.endpoint.jolokia.enabled=false")
				.run((context) -> {
					Collection<ExposableServletEndpoint> endpoints = context
							.getBean(ServletEndpointsSupplier.class).getEndpoints();
					assertThat(endpoints).isEmpty();
				});
	}

	@Test
	public void jolokiaServletWhenHasCustomConfigShouldApplyInitParams() {
		this.contextRunner
				.withPropertyValues("management.endpoint.jolokia.config.debug=true")
				.run((context) -> {
					ExposableServletEndpoint endpoint = getEndpoint(context);
					assertThat(endpoint.getEndpointServlet()).extracting("initParameters")
							.containsOnly(Collections.singletonMap("debug", "true"));
				});
	}

	private ExposableServletEndpoint getEndpoint(
			AssertableWebApplicationContext context) {
		Collection<ExposableServletEndpoint> endpoints = context
				.getBean(ServletEndpointsSupplier.class).getEndpoints();
		return endpoints.iterator().next();
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public ServletEndpointDiscoverer servletEndpointDiscoverer(
				ApplicationContext applicationContext) {
			return new ServletEndpointDiscoverer(applicationContext,
					PathMapper.useEndpointId(), Collections.emptyList());
		}

	}

}
