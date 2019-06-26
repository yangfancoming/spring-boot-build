

package org.springframework.boot.diagnostics.analyzer;

import org.junit.Test;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BeanNotOfRequiredTypeFailureAnalyzer}.
 *
 * @author Andy Wilkinson
 */
public class BeanNotOfRequiredTypeFailureAnalyzerTests {

	private final FailureAnalyzer analyzer = new BeanNotOfRequiredTypeFailureAnalyzer();

	@Test
	public void jdkProxyCausesInjectionFailure() {
		FailureAnalysis analysis = performAnalysis(JdkProxyConfiguration.class);
		assertThat(analysis.getDescription()).startsWith("The bean 'asyncBean'");
		assertThat(analysis.getDescription())
				.contains("'" + AsyncBean.class.getName() + "'");
		assertThat(analysis.getDescription())
				.endsWith(String.format("%s%n", SomeInterface.class.getName()));
	}

	private FailureAnalysis performAnalysis(Class<?> configuration) {
		FailureAnalysis analysis = this.analyzer.analyze(createFailure(configuration));
		assertThat(analysis).isNotNull();
		return analysis;
	}

	private Exception createFailure(Class<?> configuration) {
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
				configuration)) {
			fail("Expected failure did not occur");
			return null;
		}
		catch (Exception ex) {
			return ex;
		}
	}

	@Configuration
	@EnableAsync
	@Import(UserConfiguration.class)
	static class JdkProxyConfiguration {

		@Bean
		public AsyncBean asyncBean() {
			return new AsyncBean();
		}

	}

	@Configuration
	static class UserConfiguration {

		@Bean
		public AsyncBeanUser user(AsyncBean bean) {
			return new AsyncBeanUser(bean);
		}

	}

	static class AsyncBean implements SomeInterface {

		@Async
		public void foo() {

		}

		@Override
		public void bar() {

		}

	}

	interface SomeInterface {

		void bar();

	}

	static class AsyncBeanUser {

		AsyncBeanUser(AsyncBean asyncBean) {
		}

	}

}
