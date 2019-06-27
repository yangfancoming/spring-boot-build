

package org.springframework.boot.actuate.endpoint.web.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import org.springframework.boot.actuate.endpoint.http.ActuatorMediaType;
import org.springframework.boot.actuate.endpoint.invoke.convert.ConversionServiceParameterValueMapper;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.ClassUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

/**
 * {@link BlockJUnit4ClassRunner} for Spring WebFlux.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
class WebFluxEndpointsRunner extends AbstractWebEndpointRunner {

	WebFluxEndpointsRunner(Class<?> testClass) throws InitializationError {
		super(testClass, "Reactive", WebFluxEndpointsRunner::createContext);
	}

	private static ConfigurableApplicationContext createContext(List<Class<?>> classes) {
		AnnotationConfigReactiveWebServerApplicationContext context = new AnnotationConfigReactiveWebServerApplicationContext();
		classes.add(WebFluxEndpointConfiguration.class);
		context.register(ClassUtils.toClassArray(classes));
		context.refresh();
		return context;
	}

	@Configuration
	@ImportAutoConfiguration({ JacksonAutoConfiguration.class,
			WebFluxAutoConfiguration.class })
	static class WebFluxEndpointConfiguration
			implements ApplicationListener<WebServerInitializedEvent> {

		private final ApplicationContext applicationContext;

		WebFluxEndpointConfiguration(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		@Bean
		public NettyReactiveWebServerFactory netty() {
			return new NettyReactiveWebServerFactory(0);
		}

		@Bean
		public PortHolder portHolder() {
			return new PortHolder();
		}

		@Override
		public void onApplicationEvent(WebServerInitializedEvent event) {
			portHolder().setPort(event.getWebServer().getPort());
		}

		@Bean
		public HttpHandler httpHandler(ApplicationContext applicationContext) {
			return WebHttpHandlerBuilder.applicationContext(applicationContext).build();
		}

		@Bean
		public WebFluxEndpointHandlerMapping webEndpointReactiveHandlerMapping() {
			List<String> mediaTypes = Arrays.asList(MediaType.APPLICATION_JSON_VALUE,
					ActuatorMediaType.V2_JSON);
			EndpointMediaTypes endpointMediaTypes = new EndpointMediaTypes(mediaTypes,
					mediaTypes);
			WebEndpointDiscoverer discoverer = new WebEndpointDiscoverer(
					this.applicationContext, new ConversionServiceParameterValueMapper(),
					endpointMediaTypes, PathMapper.useEndpointId(),
					Collections.emptyList(), Collections.emptyList());
			return new WebFluxEndpointHandlerMapping(new EndpointMapping("/actuator"),
					discoverer.getEndpoints(), endpointMediaTypes,
					new CorsConfiguration(),
					new EndpointLinksResolver(discoverer.getEndpoints()));
		}

	}

}
