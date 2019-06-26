

package org.springframework.boot.devtools;

import ch.qos.logback.classic.Logger;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RemoteUrlPropertyExtractor}.
 *
 * @author Phillip Webb
 */
public class RemoteUrlPropertyExtractorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@After
	public void preventRunFailuresFromPollutingLoggerContext() {
		((Logger) LoggerFactory.getLogger(RemoteUrlPropertyExtractorTests.class))
				.getLoggerContext().getTurboFilterList().clear();
	}

	@Test
	public void missingUrl() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("No remote URL specified");
		doTest();
	}

	@Test
	public void malformedUrl() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Malformed URL '::://wibble'");
		doTest("::://wibble");

	}

	@Test
	public void multipleUrls() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Multiple URLs specified");
		doTest("http://localhost:8080", "http://localhost:9090");
	}

	@Test
	public void validUrl() {
		ApplicationContext context = doTest("http://localhost:8080");
		assertThat(context.getEnvironment().getProperty("remoteUrl"))
				.isEqualTo("http://localhost:8080");
		assertThat(context.getEnvironment().getProperty("spring.thymeleaf.cache"))
				.isNull();
	}

	@Test
	public void cleanValidUrl() {
		ApplicationContext context = doTest("http://localhost:8080/");
		assertThat(context.getEnvironment().getProperty("remoteUrl"))
				.isEqualTo("http://localhost:8080");
	}

	private ApplicationContext doTest(String... args) {
		SpringApplication application = new SpringApplication(Config.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.addListeners(new RemoteUrlPropertyExtractor());
		return application.run(args);
	}

	@Configuration
	static class Config {

	}

}
