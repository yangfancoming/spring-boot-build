

package org.springframework.boot.actuate.autoconfigure.web.jersey;

import java.util.Collections;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.jersey.ResourceConfigCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * {@link ManagementContextConfiguration} for Jersey infrastructure when a separate
 * management context with a web server running on a different port is required.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
@ManagementContextConfiguration(ManagementContextType.CHILD)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(ResourceConfig.class)
@ConditionalOnMissingClass("org.springframework.web.servlet.DispatcherServlet")
public class JerseyManagementChildContextConfiguration {

	private final List<ResourceConfigCustomizer> resourceConfigCustomizers;

	public JerseyManagementChildContextConfiguration(
			ObjectProvider<List<ResourceConfigCustomizer>> resourceConfigCustomizers) {
		this.resourceConfigCustomizers = resourceConfigCustomizers
				.getIfAvailable(Collections::emptyList);
	}

	@Bean
	public ServletRegistrationBean<ServletContainer> jerseyServletRegistration() {
		return new ServletRegistrationBean<>(
				new ServletContainer(endpointResourceConfig()), "/*");
	}

	@Bean
	public ResourceConfig endpointResourceConfig() {
		ResourceConfig resourceConfig = new ResourceConfig();
		for (ResourceConfigCustomizer customizer : this.resourceConfigCustomizers) {
			customizer.customize(resourceConfig);
		}
		return resourceConfig;
	}

}
