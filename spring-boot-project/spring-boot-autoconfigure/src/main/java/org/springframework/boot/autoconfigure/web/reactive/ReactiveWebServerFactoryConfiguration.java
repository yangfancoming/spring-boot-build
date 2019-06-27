

package org.springframework.boot.autoconfigure.web.reactive;

import io.undertow.Undertow;
import reactor.ipc.netty.http.server.HttpServer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration classes for reactive web servers
 * <p>
 * Those should be {@code @Import} in a regular auto-configuration class to guarantee
 * their order of execution.
 *
 * @author Brian Clozel
 */
abstract class ReactiveWebServerFactoryConfiguration {

	@Configuration
	@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
	@ConditionalOnClass({ HttpServer.class })
	static class EmbeddedNetty {

		@Bean
		public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
			return new NettyReactiveWebServerFactory();
		}

	}

	@Configuration
	@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
	@ConditionalOnClass({ org.apache.catalina.startup.Tomcat.class })
	static class EmbeddedTomcat {

		@Bean
		public TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory() {
			return new TomcatReactiveWebServerFactory();
		}

	}

	@Configuration
	@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
	@ConditionalOnClass({ org.eclipse.jetty.server.Server.class })
	static class EmbeddedJetty {

		@Bean
		public JettyReactiveWebServerFactory jettyReactiveWebServerFactory() {
			return new JettyReactiveWebServerFactory();
		}

	}

	@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
	@ConditionalOnClass({ Undertow.class })
	static class EmbeddedUndertow {

		@Bean
		public UndertowReactiveWebServerFactory undertowReactiveWebServerFactory() {
			return new UndertowReactiveWebServerFactory();
		}

	}

}
