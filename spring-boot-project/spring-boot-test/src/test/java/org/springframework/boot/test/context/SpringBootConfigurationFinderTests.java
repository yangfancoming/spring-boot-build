

package org.springframework.boot.test.context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.test.context.example.ExampleConfig;
import org.springframework.boot.test.context.example.scan.Example;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootConfigurationFinder}.
 *
 * @author Phillip Webb
 */
public class SpringBootConfigurationFinderTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private SpringBootConfigurationFinder finder = new SpringBootConfigurationFinder();

	@Test
	public void findFromClassWhenSourceIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Source must not be null");
		this.finder.findFromClass((Class<?>) null);
	}

	@Test
	public void findFromPackageWhenSourceIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Source must not be null");
		this.finder.findFromPackage((String) null);
	}

	@Test
	public void findFromPackageWhenNoConfigurationFoundShouldReturnNull() {
		Class<?> config = this.finder.findFromPackage("org.springframework.boot");
		assertThat(config).isNull();
	}

	@Test
	public void findFromClassWhenConfigurationIsFoundShouldReturnConfiguration() {
		Class<?> config = this.finder.findFromClass(Example.class);
		assertThat(config).isEqualTo(ExampleConfig.class);
	}

	@Test
	public void findFromPackageWhenConfigurationIsFoundShouldReturnConfiguration() {
		Class<?> config = this.finder
				.findFromPackage("org.springframework.boot.test.context.example.scan");
		assertThat(config).isEqualTo(ExampleConfig.class);
	}

}
