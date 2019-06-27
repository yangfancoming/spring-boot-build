

package org.springframework.boot.autoconfigure.jersey;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JerseyAutoConfiguration} when using a custom {@link Application}.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class JerseyAutoConfigurationCustomApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity("/test/hello",
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@ApplicationPath("/test")
	public static class TestApplication extends Application {

	}

	@Path("/hello")
	public static class TestController {

		@GET
		public String message() {
			return "Hello World";
		}

	}

	@Configuration
	@Import({ ServletWebServerFactoryAutoConfiguration.class,
			JerseyAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class })
	static class TestConfiguration {

		@Configuration
		public class JerseyConfiguration {

			@Bean
			public TestApplication testApplication() {
				return new TestApplication();
			}

			@Bean
			public ResourceConfig conf(TestApplication app) {
				ResourceConfig config = ResourceConfig.forApplication(app);
				config.register(TestController.class);
				return config;
			}

		}

	}

}
