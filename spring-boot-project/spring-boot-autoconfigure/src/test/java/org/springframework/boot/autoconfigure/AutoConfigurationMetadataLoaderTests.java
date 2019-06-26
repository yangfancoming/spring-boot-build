

package org.springframework.boot.autoconfigure;

import java.util.Collections;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link AutoConfigurationMetadataLoader}.
 *
 * @author Phillip Webb
 */
public class AutoConfigurationMetadataLoaderTests {

	@Test
	public void loadShouldLoadProperties() {
		assertThat(load()).isNotNull();
	}

	@Test
	public void wasProcessedWhenProcessedShouldReturnTrue() {
		assertThat(load().wasProcessed("test")).isTrue();
	}

	@Test
	public void wasProcessedWhenNotProcessedShouldReturnFalse() {
		assertThat(load().wasProcessed("testx")).isFalse();
	}

	@Test
	public void getIntegerShouldReturnValue() {
		assertThat(load().getInteger("test", "int")).isEqualTo(123);
	}

	@Test
	public void getIntegerWhenMissingShouldReturnNull() {
		assertThat(load().getInteger("test", "intx")).isNull();
	}

	@Test
	public void getIntegerWithDefaultWhenMissingShouldReturnDefault() {
		assertThat(load().getInteger("test", "intx", 345)).isEqualTo(345);
	}

	@Test
	public void getSetShouldReturnValue() {
		assertThat(load().getSet("test", "set")).containsExactly("a", "b", "c");
	}

	@Test
	public void getSetWhenMissingShouldReturnNull() {
		assertThat(load().getSet("test", "setx")).isNull();
	}

	@Test
	public void getSetWithDefaultWhenMissingShouldReturnDefault() {
		assertThat(load().getSet("test", "setx", Collections.singleton("x")))
				.containsExactly("x");
	}

	@Test
	public void getShouldReturnValue() {
		assertThat(load().get("test", "string")).isEqualTo("abc");
	}

	@Test
	public void getWhenMissingShouldReturnNull() {
		assertThat(load().get("test", "stringx")).isNull();
	}

	@Test
	public void getWithDefaultWhenMissingShouldReturnDefault() {
		assertThat(load().get("test", "stringx", "xyz")).isEqualTo("xyz");
	}

	private AutoConfigurationMetadata load() {
		return AutoConfigurationMetadataLoader.loadMetadata(null,
				"META-INF/AutoConfigurationMetadataLoaderTests.properties");
	}

}
