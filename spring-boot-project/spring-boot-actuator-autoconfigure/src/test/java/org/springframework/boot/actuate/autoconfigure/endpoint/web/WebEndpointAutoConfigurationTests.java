

package org.springframework.boot.actuate.autoconfigure.endpoint.web;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.ExposeExcludePropertyEndpointFilter;
import org.springframework.boot.actuate.endpoint.http.ActuatorMediaType;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebEndpointAutoConfiguration}.
 *
 * @author Andy Wilkinson
 * @author Yunkun Huang
 * @author Phillip Webb
 */
public class WebEndpointAutoConfigurationTests {

	private static final AutoConfigurations CONFIGURATIONS = AutoConfigurations
			.of(EndpointAutoConfiguration.class, WebEndpointAutoConfiguration.class);

	private WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withConfiguration(CONFIGURATIONS);

	@Test
	public void webApplicationConfiguresEndpointMediaTypes() {
		this.contextRunner.run((context) -> {
			EndpointMediaTypes endpointMediaTypes = context
					.getBean(EndpointMediaTypes.class);
			assertThat(endpointMediaTypes.getConsumed())
					.containsExactly(ActuatorMediaType.V2_JSON, "application/json");
		});
	}

	@Test
	public void webApplicationConfiguresPathMapper() {
		this.contextRunner
				.withPropertyValues(
						"management.endpoints.web.path-mapping.health=healthcheck")
				.run((context) -> {
					assertThat(context).hasSingleBean(PathMapper.class);
					String pathMapping = context.getBean(PathMapper.class)
							.getRootPath("health");
					assertThat(pathMapping).isEqualTo("healthcheck");
				});
	}

	@Test
	public void webApplicationConfiguresEndpointDiscoverer() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(ControllerEndpointDiscoverer.class);
			assertThat(context).hasSingleBean(WebEndpointDiscoverer.class);
		});
	}

	@Test
	public void webApplicationConfiguresExposeExcludePropertyEndpointFilter() {
		this.contextRunner.run((context) -> assertThat(context)
				.getBeans(ExposeExcludePropertyEndpointFilter.class)
				.containsKeys("webExposeExcludePropertyEndpointFilter",
						"controllerExposeExcludePropertyEndpointFilter"));
	}

	@Test
	public void contextShouldConfigureServletEndpointDiscoverer() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(ServletEndpointDiscoverer.class));
	}

	@Test
	public void contextWhenNotServletShouldNotConfigureServletEndpointDiscoverer() {
		new ApplicationContextRunner().withConfiguration(CONFIGURATIONS)
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ServletEndpointDiscoverer.class));
	}

}
