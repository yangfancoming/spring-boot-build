

package org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation;

import org.junit.Test;

import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for generating documentation describing the {@link LogFileWebEndpoint}.
 *
 * @author Andy Wilkinson
 */
@TestPropertySource(properties = "logging.file=src/test/resources/org/springframework/boot/actuate/autoconfigure/endpoint/web/documentation/sample.log")
public class LogFileWebEndpointDocumentationTests
		extends MockMvcEndpointDocumentationTests {

	@Test
	public void logFile() throws Exception {
		this.mockMvc.perform(get("/actuator/logfile")).andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("logfile/entire"));
	}

	@Test
	public void logFileRange() throws Exception {
		this.mockMvc.perform(get("/actuator/logfile").header("Range", "bytes=0-1023"))
				.andExpect(status().isPartialContent())
				.andDo(MockMvcRestDocumentation.document("logfile/range"));
	}

	@Configuration
	@Import(BaseDocumentationConfiguration.class)
	static class TestConfiguration {

		@Bean
		public LogFileWebEndpoint endpoint(Environment environment) {
			return new LogFileWebEndpoint(environment);
		}

	}

}
