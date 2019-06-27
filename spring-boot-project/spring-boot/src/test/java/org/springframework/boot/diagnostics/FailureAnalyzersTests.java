

package org.springframework.boot.diagnostics;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link FailureAnalyzers}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class FailureAnalyzersTests {

	private static AwareFailureAnalyzer failureAnalyzer;

	@Before
	public void configureMock() {
		failureAnalyzer = mock(AwareFailureAnalyzer.class);
	}

	@Test
	public void analyzersAreLoadedAndCalled() {
		RuntimeException failure = new RuntimeException();
		analyzeAndReport("basic.factories", failure);
		verify(failureAnalyzer, times(2)).analyze(failure);
	}

	@Test
	public void beanFactoryIsInjectedIntoBeanFactoryAwareFailureAnalyzers() {
		RuntimeException failure = new RuntimeException();
		analyzeAndReport("basic.factories", failure);
		verify(failureAnalyzer).setBeanFactory(any(BeanFactory.class));
	}

	@Test
	public void environmentIsInjectedIntoEnvironmentAwareFailureAnalyzers() {
		RuntimeException failure = new RuntimeException();
		analyzeAndReport("basic.factories", failure);
		verify(failureAnalyzer).setEnvironment(any(Environment.class));
	}

	@Test
	public void analyzerThatFailsDuringInitializationDoesNotPreventOtherAnalyzersFromBeingCalled() {
		RuntimeException failure = new RuntimeException();
		analyzeAndReport("broken-initialization.factories", failure);
		verify(failureAnalyzer, times(1)).analyze(failure);
	}

	@Test
	public void analyzerThatFailsDuringAnalysisDoesNotPreventOtherAnalyzersFromBeingCalled() {
		RuntimeException failure = new RuntimeException();
		analyzeAndReport("broken-analysis.factories", failure);
		verify(failureAnalyzer, times(1)).analyze(failure);
	}

	private void analyzeAndReport(String factoriesName, Throwable failure) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		ClassLoader classLoader = new CustomSpringFactoriesClassLoader(factoriesName);
		new FailureAnalyzers(context, classLoader).reportException(failure);
	}

	static class BasicFailureAnalyzer implements FailureAnalyzer {

		@Override
		public FailureAnalysis analyze(Throwable failure) {
			return failureAnalyzer.analyze(failure);
		}

	}

	static class BrokenInitializationFailureAnalyzer implements FailureAnalyzer {

		static {
			Object foo = null;
			foo.toString();
		}

		@Override
		public FailureAnalysis analyze(Throwable failure) {
			return null;
		}

	}

	static class BrokenAnalysisFailureAnalyzer implements FailureAnalyzer {

		@Override
		public FailureAnalysis analyze(Throwable failure) {
			throw new NoClassDefFoundError();
		}

	}

	interface AwareFailureAnalyzer
			extends BeanFactoryAware, EnvironmentAware, FailureAnalyzer {

	}

	static class StandardAwareFailureAnalyzer extends BasicFailureAnalyzer
			implements AwareFailureAnalyzer {

		@Override
		public void setEnvironment(Environment environment) {
			failureAnalyzer.setEnvironment(environment);
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			failureAnalyzer.setBeanFactory(beanFactory);
		}

	}

	static class CustomSpringFactoriesClassLoader extends ClassLoader {

		private final String factoriesName;

		CustomSpringFactoriesClassLoader(String factoriesName) {
			super(CustomSpringFactoriesClassLoader.class.getClassLoader());
			this.factoriesName = factoriesName;
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			if ("META-INF/spring.factories".equals(name)) {
				return super.getResources(
						"failure-analyzers-tests/" + this.factoriesName);
			}
			return super.getResources(name);
		}

	}

}
