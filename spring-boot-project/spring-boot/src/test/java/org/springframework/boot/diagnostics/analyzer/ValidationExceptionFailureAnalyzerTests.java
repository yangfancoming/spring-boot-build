

package org.springframework.boot.diagnostics.analyzer;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.annotation.Validated;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ValidationExceptionFailureAnalyzer}
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("hibernate-validator-*.jar")
public class ValidationExceptionFailureAnalyzerTests {

	@Test
	public void validatedPropertiesTest() {
		try {
			new AnnotationConfigApplicationContext(TestConfiguration.class).close();
			fail("Expected failure did not occur");
		}
		catch (Exception ex) {
			FailureAnalysis analysis = new ValidationExceptionFailureAnalyzer()
					.analyze(ex);
			assertThat(analysis).isNotNull();
		}
	}

	@Test
	public void nonValidatedPropertiesTest() {
		new AnnotationConfigApplicationContext(NonValidatedTestConfiguration.class)
				.close();
	}

	@EnableConfigurationProperties(TestProperties.class)
	static class TestConfiguration {

		TestConfiguration(TestProperties testProperties) {
		}

	}

	@ConfigurationProperties("test")
	@Validated
	private static class TestProperties {

	}

	@EnableConfigurationProperties(NonValidatedTestProperties.class)
	static class NonValidatedTestConfiguration {

		NonValidatedTestConfiguration(NonValidatedTestProperties testProperties) {
		}

	}

	@ConfigurationProperties("test")
	private static class NonValidatedTestProperties {

	}

}
