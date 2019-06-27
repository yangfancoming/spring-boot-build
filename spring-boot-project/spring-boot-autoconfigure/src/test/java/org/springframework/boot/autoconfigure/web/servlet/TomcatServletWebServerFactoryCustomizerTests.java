

package org.springframework.boot.autoconfigure.web.servlet;

import org.apache.catalina.Context;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TomcatServletWebServerFactoryCustomizer}.
 *
 * @author Phillip Webb
 */
public class TomcatServletWebServerFactoryCustomizerTests {

	private TomcatServletWebServerFactoryCustomizer customizer;

	private MockEnvironment environment;

	private ServerProperties serverProperties;

	@Before
	public void setup() {
		this.environment = new MockEnvironment();
		this.serverProperties = new ServerProperties();
		ConfigurationPropertySources.attach(this.environment);
		this.customizer = new TomcatServletWebServerFactoryCustomizer(
				this.serverProperties);
	}

	@Test
	public void customTldSkip() {
		bind("server.tomcat.additional-tld-skip-patterns=foo.jar,bar.jar");
		testCustomTldSkip("foo.jar", "bar.jar");
	}

	@Test
	public void customTldSkipAsList() {
		bind("server.tomcat.additional-tld-skip-patterns[0]=biz.jar",
				"server.tomcat.additional-tld-skip-patterns[1]=bah.jar");
		testCustomTldSkip("biz.jar", "bah.jar");
	}

	private void testCustomTldSkip(String... expectedJars) {
		TomcatServletWebServerFactory factory = customizeAndGetFactory();
		assertThat(factory.getTldSkipPatterns()).contains(expectedJars);
		assertThat(factory.getTldSkipPatterns()).contains("junit-*.jar",
				"spring-boot-*.jar");
	}

	@Test
	public void redirectContextRootCanBeConfigured() {
		bind("server.tomcat.redirect-context-root=false");
		ServerProperties.Tomcat tomcat = this.serverProperties.getTomcat();
		assertThat(tomcat.getRedirectContextRoot()).isFalse();
		TomcatWebServer server = customizeAndGetServer();
		Context context = (Context) server.getTomcat().getHost().findChildren()[0];
		assertThat(context.getMapperContextRootRedirectEnabled()).isFalse();
	}

	@Test
	public void useRelativeRedirectsCanBeConfigured() {
		bind("server.tomcat.use-relative-redirects=true");
		assertThat(this.serverProperties.getTomcat().getUseRelativeRedirects()).isTrue();
		TomcatWebServer server = customizeAndGetServer();
		Context context = (Context) server.getTomcat().getHost().findChildren()[0];
		assertThat(context.getUseRelativeRedirects()).isTrue();
	}

	private void bind(String... inlinedProperties) {
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(this.environment,
				inlinedProperties);
		new Binder(ConfigurationPropertySources.get(this.environment)).bind("server",
				Bindable.ofInstance(this.serverProperties));
	}

	private TomcatWebServer customizeAndGetServer() {
		TomcatServletWebServerFactory factory = customizeAndGetFactory();
		return (TomcatWebServer) factory.getWebServer();
	}

	private TomcatServletWebServerFactory customizeAndGetFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(0);
		this.customizer.customize(factory);
		return factory;
	}

}
