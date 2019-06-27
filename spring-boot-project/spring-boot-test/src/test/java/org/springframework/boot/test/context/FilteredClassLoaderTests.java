

package org.springframework.boot.test.context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FilteredClassLoader}.
 *
 * @author Phillip Webb
 */
public class FilteredClassLoaderTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void loadClassWhenFilteredOnPackageShouldThrowClassNotFound()
			throws Exception {
		FilteredClassLoader classLoader = new FilteredClassLoader(
				FilteredClassLoaderTests.class.getPackage().getName());
		this.thrown.expect(ClassNotFoundException.class);
		classLoader.loadClass(getClass().getName());
		classLoader.close();
	}

	@Test
	public void loadClassWhenFilteredOnClassShouldThrowClassNotFound() throws Exception {
		try (FilteredClassLoader classLoader = new FilteredClassLoader(
				FilteredClassLoaderTests.class)) {
			this.thrown.expect(ClassNotFoundException.class);
			classLoader.loadClass(getClass().getName());
		}
	}

	@Test
	public void loadClassWhenNotFilteredShouldLoadClass() throws Exception {
		FilteredClassLoader classLoader = new FilteredClassLoader((className) -> false);
		Class<?> loaded = classLoader.loadClass(getClass().getName());
		assertThat(loaded.getName()).isEqualTo(getClass().getName());
		classLoader.close();
	}

}
