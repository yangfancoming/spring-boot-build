

package org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation;

import java.util.Collection;
import java.util.regex.Pattern;

import org.junit.Test;

import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTaskHolder;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for generating documentation describing the {@link ScheduledTasksEndpoint}.
 *
 * @author Andy Wilkinson
 */
public class ScheduledTasksEndpointDocumentationTests
		extends MockMvcEndpointDocumentationTests {

	@Test
	public void scheduledTasks() throws Exception {
		this.mockMvc.perform(get("/actuator/scheduledtasks")).andExpect(status().isOk())
				.andDo(document("scheduled-tasks",
						preprocessResponse(replacePattern(Pattern.compile(
								"org.*\\.ScheduledTasksEndpointDocumentationTests\\$"
										+ "TestConfiguration"),
								"com.example.Processor")),
						responseFields(
								fieldWithPath("cron").description("Cron tasks, if any."),
								targetFieldWithPrefix("cron.[]"),
								fieldWithPath("cron.[].expression")
										.description("Cron expression."),
								fieldWithPath("fixedDelay")
										.description("Fixed delay tasks, if any."),
								targetFieldWithPrefix("fixedDelay.[]"),
								initialDelayWithPrefix("fixedDelay.[]."),
								fieldWithPath("fixedDelay.[].interval").description(
										"Interval, in milliseconds, between the end of the last"
												+ " execution and the start of the next."),
								fieldWithPath("fixedRate")
										.description("Fixed rate tasks, if any."),
								targetFieldWithPrefix("fixedRate.[]."),
								fieldWithPath("fixedRate.[].interval").description(
										"Interval, in milliseconds, between the start of each execution."),
								initialDelayWithPrefix("fixedRate.[]."))));
	}

	private FieldDescriptor targetFieldWithPrefix(String prefix) {
		return fieldWithPath(prefix + "runnable.target")
				.description("Target that will be executed.");
	}

	private FieldDescriptor initialDelayWithPrefix(String prefix) {
		return fieldWithPath(prefix + "initialDelay")
				.description("Delay, in milliseconds, before first execution.");
	}

	@Configuration
	@EnableScheduling
	@Import(BaseDocumentationConfiguration.class)
	static class TestConfiguration {

		@Bean
		public ScheduledTasksEndpoint endpoint(Collection<ScheduledTaskHolder> holders) {
			return new ScheduledTasksEndpoint(holders);
		}

		@Scheduled(cron = "0 0 0/3 1/1 * ?")
		public void processOrders() {

		}

		@Scheduled(fixedDelay = 5000, initialDelay = 5000)
		public void purge() {

		}

		@Scheduled(fixedRate = 3000, initialDelay = 10000)
		public void retrieveIssues() {

		}

	}

}
