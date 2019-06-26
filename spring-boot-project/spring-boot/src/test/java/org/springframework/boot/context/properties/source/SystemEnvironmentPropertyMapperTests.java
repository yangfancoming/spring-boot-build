

package org.springframework.boot.context.properties.source;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SystemEnvironmentPropertyMapper}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class SystemEnvironmentPropertyMapperTests extends AbstractPropertyMapperTests {

	@Override
	protected PropertyMapper getMapper() {
		return SystemEnvironmentPropertyMapper.INSTANCE;
	}

	@Test
	public void mapFromStringShouldReturnBestGuess() {
		assertThat(namesFromString("SERVER")).containsExactly("server");
		assertThat(namesFromString("SERVER_PORT")).containsExactly("server.port");
		assertThat(namesFromString("HOST_0")).containsExactly("host[0]");
		assertThat(namesFromString("HOST_0_1")).containsExactly("host[0][1]");
		assertThat(namesFromString("HOST_0_NAME")).containsExactly("host[0].name");
		assertThat(namesFromString("HOST_F00_NAME")).containsExactly("host.f00.name");
		assertThat(namesFromString("S-ERVER")).containsExactly("s-erver");
	}

	@Test
	public void mapFromConfigurationShouldReturnBestGuess() {
		assertThat(namesFromConfiguration("server")).containsExactly("SERVER");
		assertThat(namesFromConfiguration("server.port")).containsExactly("SERVER_PORT");
		assertThat(namesFromConfiguration("host[0]")).containsExactly("HOST_0");
		assertThat(namesFromConfiguration("host[0][1]")).containsExactly("HOST_0_1");
		assertThat(namesFromConfiguration("host[0].name")).containsExactly("HOST_0_NAME");
		assertThat(namesFromConfiguration("host.f00.name"))
				.containsExactly("HOST_F00_NAME");
		assertThat(namesFromConfiguration("foo.the-bar")).containsExactly("FOO_THEBAR",
				"FOO_THE_BAR");
	}

	@Test
	public void underscoreShouldNotMapToEmptyString() {
		PropertyMapping[] mappings = getMapper().map("_");
		boolean applicable = false;
		for (PropertyMapping mapping : mappings) {
			applicable = mapping.isApplicable(ConfigurationPropertyName.of(""));
		}
		assertThat(applicable).isFalse();
	}

	@Test
	public void underscoreWithWhitespaceShouldNotMapToEmptyString() {
		PropertyMapping[] mappings = getMapper().map("  _");
		boolean applicable = false;
		for (PropertyMapping mapping : mappings) {
			applicable = mapping.isApplicable(ConfigurationPropertyName.of(""));
		}
		assertThat(applicable).isFalse();
	}

}
