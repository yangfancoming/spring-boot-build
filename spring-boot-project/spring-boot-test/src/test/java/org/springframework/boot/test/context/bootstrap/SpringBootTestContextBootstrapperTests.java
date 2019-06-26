

package org.springframework.boot.test.context.bootstrap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapContext;
import org.springframework.test.context.CacheAwareContextLoaderDelegate;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringBootTestContextBootstrapper}.
 *
 * @author Andy Wilkinson
 */
public class SpringBootTestContextBootstrapperTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void springBootTestWithANonMockWebEnvironmentAndWebAppConfigurationFailsFast() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("@WebAppConfiguration should only be used with "
				+ "@SpringBootTest when @SpringBootTest is configured with a mock web "
				+ "environment. Please remove @WebAppConfiguration or reconfigure "
				+ "@SpringBootTest.");
		buildTestContext(SpringBootTestNonMockWebEnvironmentAndWebAppConfiguration.class);
	}

	@Test
	public void springBootTestWithAMockWebEnvironmentCanBeUsedWithWebAppConfiguration() {
		buildTestContext(SpringBootTestMockWebEnvironmentAndWebAppConfiguration.class);
	}

	@SuppressWarnings("rawtypes")
	private void buildTestContext(Class<?> testClass) {
		SpringBootTestContextBootstrapper bootstrapper = new SpringBootTestContextBootstrapper();
		BootstrapContext bootstrapContext = mock(BootstrapContext.class);
		bootstrapper.setBootstrapContext(bootstrapContext);
		given((Class) bootstrapContext.getTestClass()).willReturn(testClass);
		CacheAwareContextLoaderDelegate contextLoaderDelegate = mock(
				CacheAwareContextLoaderDelegate.class);
		given(bootstrapContext.getCacheAwareContextLoaderDelegate())
				.willReturn(contextLoaderDelegate);
		bootstrapper.buildTestContext();
	}

	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@WebAppConfiguration
	private static class SpringBootTestNonMockWebEnvironmentAndWebAppConfiguration {

	}

	@SpringBootTest
	@WebAppConfiguration
	private static class SpringBootTestMockWebEnvironmentAndWebAppConfiguration {

	}

}
