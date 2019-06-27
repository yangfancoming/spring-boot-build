

package org.springframework.boot.actuate.autoconfigure.web.mappings;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.web.mappings.MappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.actuate.web.mappings.reactive.DispatcherHandlersMappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletsMappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.FiltersMappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.ServletsMappingDescriptionProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link MappingsEndpoint}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Configuration
public class MappingsEndpointAutoConfiguration {

	@Bean
	@ConditionalOnEnabledEndpoint
	public MappingsEndpoint mappingsEndpoint(ApplicationContext applicationContext,
			ObjectProvider<Collection<MappingDescriptionProvider>> descriptionProviders) {
		return new MappingsEndpoint(
				descriptionProviders.getIfAvailable(Collections::emptyList),
				applicationContext);
	}

	@Configuration
	@ConditionalOnWebApplication(type = Type.SERVLET)
	static class ServletWebConfiguration {

		@Bean
		ServletsMappingDescriptionProvider servletMappingDescriptionProvider() {
			return new ServletsMappingDescriptionProvider();
		}

		@Bean
		FiltersMappingDescriptionProvider filterMappingDescriptionProvider() {
			return new FiltersMappingDescriptionProvider();
		}

		@Configuration
		@ConditionalOnClass(DispatcherServlet.class)
		@ConditionalOnBean(DispatcherServlet.class)
		static class SpringMvcConfiguration {

			@Bean
			DispatcherServletsMappingDescriptionProvider dispatcherServletMappingDescriptionProvider() {
				return new DispatcherServletsMappingDescriptionProvider();
			}

		}

	}

	@Configuration
	@ConditionalOnWebApplication(type = Type.REACTIVE)
	@ConditionalOnClass(DispatcherHandler.class)
	@ConditionalOnBean(DispatcherHandler.class)
	static class ReactiveWebConfiguration {

		@Bean
		public DispatcherHandlersMappingDescriptionProvider dispatcherHandlerMappingDescriptionProvider() {
			return new DispatcherHandlersMappingDescriptionProvider();
		}

	}

}
