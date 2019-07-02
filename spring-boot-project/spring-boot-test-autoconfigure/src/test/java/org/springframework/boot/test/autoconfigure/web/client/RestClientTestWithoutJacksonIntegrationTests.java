

package org.springframework.boot.test.autoconfigure.web.client;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RestClientTest} without Jackson.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("jackson-*.jar")
public class RestClientTestWithoutJacksonIntegrationTests {

	@Test
	public void restClientTestCanBeUsedWhenJacksonIsNotOnTheClassPath() {
		assertThat(ClassUtils.isPresent("com.fasterxml.jackson.databind.Module",
				getClass().getClassLoader())).isFalse();
		Result result = JUnitCore
				.runClasses(RestClientTestWithComponentIntegrationTests.class);
		assertThat(result.getFailureCount()).isEqualTo(0);
		assertThat(result.getRunCount()).isGreaterThan(0);
	}

}
