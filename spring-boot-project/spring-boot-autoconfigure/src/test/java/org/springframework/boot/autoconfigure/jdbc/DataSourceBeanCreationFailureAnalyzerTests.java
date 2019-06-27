

package org.springframework.boot.autoconfigure.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DataSourceBeanCreationFailureAnalyzer}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions({ "h2-*.jar", "hsqldb-*.jar" })
public class DataSourceBeanCreationFailureAnalyzerTests {

	private final MockEnvironment environment = new MockEnvironment();

	@Test
	public void failureAnalysisIsPerformed() {
		FailureAnalysis failureAnalysis = performAnalysis(TestConfiguration.class);
		assertThat(failureAnalysis.getDescription()).contains(
				"'url' attribute is not specified",
				"no embedded datasource could be configured",
				"Failed to determine a suitable driver class");
		assertThat(failureAnalysis.getAction()).contains(
				"If you want an embedded database (H2, HSQL or Derby), please put it on the classpath",
				"If you have database settings to be loaded from a particular profile you may need to activate it",
				"(no profiles are currently active)");
	}

	@Test
	public void failureAnalysisIsPerformedWithActiveProfiles() {
		this.environment.setActiveProfiles("first", "second");
		FailureAnalysis failureAnalysis = performAnalysis(TestConfiguration.class);
		assertThat(failureAnalysis.getAction())
				.contains("(the profiles first,second are currently active)");
	}

	private FailureAnalysis performAnalysis(Class<?> configuration) {
		BeanCreationException failure = createFailure(configuration);
		assertThat(failure).isNotNull();
		DataSourceBeanCreationFailureAnalyzer failureAnalyzer = new DataSourceBeanCreationFailureAnalyzer();
		failureAnalyzer.setEnvironment(this.environment);
		return failureAnalyzer.analyze(failure);
	}

	private BeanCreationException createFailure(Class<?> configuration) {
		try {
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
			context.setEnvironment(this.environment);
			context.register(configuration);
			context.refresh();
			context.close();
			return null;
		}
		catch (BeanCreationException ex) {
			return ex;
		}
	}

	@Configuration
	@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
	static class TestConfiguration {

	}

}
