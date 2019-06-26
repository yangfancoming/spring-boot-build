

package org.springframework.boot.testsupport.runner.classpath;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;

/**
 * Tests for {@link ModifiedClassPathRunner} excluding entries from the class path.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("hibernate-validator-*.jar")
public class ModifiedClassPathRunnerExclusionsTests {

	private static final String EXCLUDED_RESOURCE = "META-INF/services/"
			+ "javax.validation.spi.ValidationProvider";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void entriesAreFilteredFromTestClassClassLoader() {
		assertThat(getClass().getClassLoader().getResource(EXCLUDED_RESOURCE)).isNull();
	}

	@Test
	public void entriesAreFilteredFromThreadContextClassLoader() {
		assertThat(Thread.currentThread().getContextClassLoader()
				.getResource(EXCLUDED_RESOURCE)).isNull();
	}

	@Test
	public void testsThatUseHamcrestWorkCorrectly() {
		this.thrown.expect(isA(IllegalStateException.class));
		throw new IllegalStateException();
	}

}
