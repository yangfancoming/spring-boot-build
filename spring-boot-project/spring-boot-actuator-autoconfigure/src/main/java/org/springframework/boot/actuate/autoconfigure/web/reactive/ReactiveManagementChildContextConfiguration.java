

package org.springframework.boot.actuate.autoconfigure.web.reactive;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.embedded.JettyWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.embedded.UndertowWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryCustomizer;
import org.springframework.boot.web.reactive.server.ConfigurableReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

/**
 * {@link ManagementContextConfiguration} for reactive web infrastructure when a separate
 * management context with a web server running on a different port is required.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
@EnableWebFlux
@ManagementContextConfiguration(ManagementContextType.CHILD)
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class ReactiveManagementChildContextConfiguration {

	@Bean
	public ReactiveManagementWebServerFactoryCustomizer reactiveManagementWebServerFactoryCustomizer(
			ListableBeanFactory beanFactory) {
		return new ReactiveManagementWebServerFactoryCustomizer(beanFactory);
	}

	@Bean
	public HttpHandler httpHandler(ApplicationContext applicationContext) {
		return WebHttpHandlerBuilder.applicationContext(applicationContext).build();
	}

	class ReactiveManagementWebServerFactoryCustomizer extends
			ManagementWebServerFactoryCustomizer<ConfigurableReactiveWebServerFactory> {

		ReactiveManagementWebServerFactoryCustomizer(ListableBeanFactory beanFactory) {
			super(beanFactory, ReactiveWebServerFactoryCustomizer.class,
					TomcatWebServerFactoryCustomizer.class,
					JettyWebServerFactoryCustomizer.class,
					UndertowWebServerFactoryCustomizer.class);
		}

	}

}
