

package org.springframework.boot.autoconfigure.jersey;

import java.nio.charset.StandardCharsets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.tomcat.util.buf.UDecoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfigurationServletContainerTests.Application;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that verify the behavior when deployed to a Servlet container where Jersey may
 * have already initialized itself.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class JerseyAutoConfigurationServletContainerTests {

	@ClassRule
	public static OutputCapture output = new OutputCapture();

	@Test
	public void existingJerseyServletIsAmended() {
		assertThat(output.toString())
				.contains("Configuring existing registration for Jersey servlet");
		assertThat(output.toString()).contains(
				"Servlet " + Application.class.getName() + " was not registered");
	}

	@ImportAutoConfiguration({ ServletWebServerFactoryAutoConfiguration.class,
			JerseyAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class })
	@Import(ContainerConfiguration.class)
	@Path("/hello")
	public static class Application extends ResourceConfig {

		@Value("${message:World}")
		private String msg;

		public Application() {
			register(Application.class);
		}

		@GET
		public String message() {
			return "Hello " + this.msg;
		}

	}

	@Configuration
	public static class ContainerConfiguration {

		@Bean
		public TomcatServletWebServerFactory tomcat() {
			return new TomcatServletWebServerFactory() {

				@Override
				protected void postProcessContext(Context context) {
					Wrapper jerseyServlet = context.createWrapper();
					String servletName = Application.class.getName();
					jerseyServlet.setName(servletName);
					jerseyServlet.setServletClass(ServletContainer.class.getName());
					jerseyServlet.setServlet(new ServletContainer());
					jerseyServlet.setOverridable(false);
					context.addChild(jerseyServlet);
					String pattern = UDecoder.URLDecode("/*", StandardCharsets.UTF_8);
					context.addServletMappingDecoded(pattern, servletName);
				}

			};
		}

	}

}
