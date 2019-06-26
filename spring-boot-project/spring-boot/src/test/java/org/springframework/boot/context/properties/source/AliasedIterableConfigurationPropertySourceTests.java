

package org.springframework.boot.context.properties.source;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AliasedConfigurationPropertySource}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class AliasedIterableConfigurationPropertySourceTests
		extends AliasedConfigurationPropertySourceTests {

	@Test
	public void streamShouldIncludeAliases() {
		MockConfigurationPropertySource source = new MockConfigurationPropertySource();
		source.put("foo.bar", "bing");
		source.put("foo.baz", "biff");
		IterableConfigurationPropertySource aliased = source
				.withAliases(new ConfigurationPropertyNameAliases("foo.bar", "foo.bar1"));
		assertThat(aliased.stream()).containsExactly(
				ConfigurationPropertyName.of("foo.bar"),
				ConfigurationPropertyName.of("foo.bar1"),
				ConfigurationPropertyName.of("foo.baz"));
	}

}
