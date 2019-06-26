

package org.springframework.boot.diagnostics;

import javax.annotation.PostConstruct;

import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.testsupport.rule.OutputCapture;
import org.springframework.boot.web.server.PortInUseException;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Integration tests for {@link FailureAnalyzers}.
 *
 * @author Andy Wilkinson
 */
public class FailureAnalyzersIntegrationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void analysisIsPerformed() {
		try {
			new SpringApplicationBuilder(TestConfiguration.class)
					.web(WebApplicationType.NONE).run();
			fail("Application started successfully");
		}
		catch (Exception ex) {
			assertThat(this.outputCapture.toString())
					.contains("APPLICATION FAILED TO START");
		}
	}

	@Configuration
	static class TestConfiguration {

		@PostConstruct
		public void fail() {
			throw new PortInUseException(8080);
		}

	}

}
