

package org.springframework.boot.context.logging;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.testsupport.rule.OutputCapture;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link LoggingApplicationListener}.
 *
 * @author Stephane Nicoll
 */
public class LoggingApplicationListenerIntegrationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void loggingSystemRegisteredInTheContext() {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				SampleService.class).web(WebApplicationType.NONE).run()) {
			SampleService service = context.getBean(SampleService.class);
			assertThat(service.loggingSystem).isNotNull();
		}
	}

	@Test
	public void loggingPerformedDuringChildApplicationStartIsNotLost() {
		new SpringApplicationBuilder(Config.class).web(WebApplicationType.NONE)
				.child(Config.class).web(WebApplicationType.NONE)
				.listeners(new ApplicationListener<ApplicationStartingEvent>() {

					private final Logger logger = LoggerFactory.getLogger(getClass());

					@Override
					public void onApplicationEvent(ApplicationStartingEvent event) {
						this.logger.info("Child application starting");
					}

				}).run();
		assertThat(this.outputCapture.toString()).contains("Child application starting");
	}

	@Component
	static class SampleService {

		private final LoggingSystem loggingSystem;

		SampleService(LoggingSystem loggingSystem) {
			this.loggingSystem = loggingSystem;
		}

	}

	static class Config {

	}

}
