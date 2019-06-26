

package org.springframework.boot.testsupport.runner.classpath;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ModifiedClassPathRunner} overriding entries on the class path.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathOverrides("org.springframework:spring-context:4.1.0.RELEASE")
public class ModifiedClassPathRunnerOverridesTests {

	@Test
	public void classesAreLoadedFromOverride() {
		assertThat(ApplicationContext.class.getProtectionDomain().getCodeSource()
				.getLocation().toString()).endsWith("spring-context-4.1.0.RELEASE.jar");
	}

	@Test
	public void classesAreLoadedFromTransitiveDependencyOfOverride() {
		assertThat(StringUtils.class.getProtectionDomain().getCodeSource().getLocation()
				.toString()).endsWith("spring-core-4.1.0.RELEASE.jar");
	}

}
