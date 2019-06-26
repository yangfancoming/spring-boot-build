

package org.springframework.boot.autoconfigure;

import org.junit.Test;

import org.springframework.boot.context.annotation.Configurations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AutoConfigurations}.
 *
 * @author Phillip Webb
 */
public class AutoConfigurationsTests {

	@Test
	public void ofShouldCreateOrderedConfigurations() {
		Configurations configurations = AutoConfigurations.of(AutoConfigureA.class,
				AutoConfigureB.class);
		assertThat(Configurations.getClasses(configurations))
				.containsExactly(AutoConfigureB.class, AutoConfigureA.class);
	}

	@AutoConfigureAfter(AutoConfigureB.class)
	public static class AutoConfigureA {

	}

	public static class AutoConfigureB {

	}

}
