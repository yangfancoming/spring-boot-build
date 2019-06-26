

package org.springframework.boot.actuate.logging;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import org.springframework.boot.actuate.endpoint.web.test.WebEndpointRunners;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.FileCopyUtils;

/**
 * Integration tests for {@link LogFileWebEndpoint} exposed by Jersey, Spring MVC, and
 * WebFlux.
 *
 * @author Andy Wilkinson
 */
@RunWith(WebEndpointRunners.class)
public class LogFileWebEndpointWebIntegrationTests {

	private static ConfigurableApplicationContext context;

	private static WebTestClient client;

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	private File logFile;

	@Before
	public void setUp() throws IOException {
		this.logFile = this.temp.newFile();
		FileCopyUtils.copy("--TEST--".getBytes(), this.logFile);
	}

	@Test
	public void getRequestProduces404ResponseWhenLogFileNotFound() {
		client.get().uri("/actuator/logfile").exchange().expectStatus().isNotFound();
	}

	@Test
	public void getRequestProducesResponseWithLogFile() {
		TestPropertyValues.of("logging.file:" + this.logFile.getAbsolutePath())
				.applyTo(context);
		client.get().uri("/actuator/logfile").exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("--TEST--");
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public LogFileWebEndpoint logFileEndpoint(Environment environment) {
			return new LogFileWebEndpoint(environment);
		}

	}

}
