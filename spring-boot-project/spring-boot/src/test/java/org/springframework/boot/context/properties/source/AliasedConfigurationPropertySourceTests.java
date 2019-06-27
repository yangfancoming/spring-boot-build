

package org.springframework.boot.context.properties.source;

import org.junit.Test;
import org.mockito.Answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * Tests for {@link AliasedConfigurationPropertySource}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class AliasedConfigurationPropertySourceTests {

	@Test
	public void getConfigurationPropertyShouldConsiderAliases() {
		MockConfigurationPropertySource source = new MockConfigurationPropertySource();
		source.put("foo.bar", "bing");
		source.put("foo.baz", "biff");
		ConfigurationPropertySource aliased = source.nonIterable()
				.withAliases(new ConfigurationPropertyNameAliases("foo.bar", "foo.bar1"));
		assertThat(getValue(aliased, "foo.bar")).isEqualTo("bing");
		assertThat(getValue(aliased, "foo.bar1")).isEqualTo("bing");
	}

	@Test
	public void getConfigurationPropertyWhenNotAliasesShouldReturnValue() {
		MockConfigurationPropertySource source = new MockConfigurationPropertySource();
		source.put("foo.bar", "bing");
		source.put("foo.baz", "biff");
		ConfigurationPropertySource aliased = source.nonIterable()
				.withAliases(new ConfigurationPropertyNameAliases("foo.bar", "foo.bar1"));
		assertThat(getValue(aliased, "foo.baz")).isEqualTo("biff");
	}

	@Test
	public void containsDescendantOfWhenSourceReturnsUnknownShouldReturnUnknown() {
		ConfigurationPropertyName name = ConfigurationPropertyName.of("foo");
		ConfigurationPropertySource source = mock(ConfigurationPropertySource.class,
				withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS));
		given(source.containsDescendantOf(name))
				.willReturn(ConfigurationPropertyState.UNKNOWN);
		ConfigurationPropertySource aliased = source
				.withAliases(new ConfigurationPropertyNameAliases("foo.bar", "foo.bar1"));
		assertThat(aliased.containsDescendantOf(name))
				.isEqualTo(ConfigurationPropertyState.UNKNOWN);
	}

	@Test
	public void containsDescendantOfWhenSourceReturnsPresentShouldReturnPresent() {
		ConfigurationPropertyName name = ConfigurationPropertyName.of("foo");
		ConfigurationPropertySource source = mock(ConfigurationPropertySource.class,
				withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS));
		given(source.containsDescendantOf(name))
				.willReturn(ConfigurationPropertyState.PRESENT);
		given(source.containsDescendantOf(ConfigurationPropertyName.of("bar")))
				.willReturn(ConfigurationPropertyState.UNKNOWN);
		ConfigurationPropertySource aliased = source
				.withAliases(new ConfigurationPropertyNameAliases("foo.bar", "foo.bar1"));
		assertThat(aliased.containsDescendantOf(name))
				.isEqualTo(ConfigurationPropertyState.PRESENT);
	}

	@Test
	public void containsDescendantOfWhenAllAreAbsentShouldReturnAbsent() {
		ConfigurationPropertyName name = ConfigurationPropertyName.of("foo");
		ConfigurationPropertySource source = mock(ConfigurationPropertySource.class,
				withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS));
		given(source.containsDescendantOf(name))
				.willReturn(ConfigurationPropertyState.ABSENT);
		given(source.containsDescendantOf(ConfigurationPropertyName.of("bar")))
				.willReturn(ConfigurationPropertyState.ABSENT);
		ConfigurationPropertySource aliased = source
				.withAliases(new ConfigurationPropertyNameAliases("foo", "bar"));
		assertThat(aliased.containsDescendantOf(name))
				.isEqualTo(ConfigurationPropertyState.ABSENT);
	}

	@Test
	public void containsDescendantOfWhenAnyIsPresentShouldReturnPresent() {
		ConfigurationPropertyName name = ConfigurationPropertyName.of("foo");
		ConfigurationPropertySource source = mock(ConfigurationPropertySource.class,
				withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS));
		given(source.containsDescendantOf(name))
				.willReturn(ConfigurationPropertyState.ABSENT);
		given(source.containsDescendantOf(ConfigurationPropertyName.of("bar")))
				.willReturn(ConfigurationPropertyState.PRESENT);
		ConfigurationPropertySource aliased = source
				.withAliases(new ConfigurationPropertyNameAliases("foo", "bar"));
		assertThat(aliased.containsDescendantOf(name))
				.isEqualTo(ConfigurationPropertyState.PRESENT);
	}

	private Object getValue(ConfigurationPropertySource source, String name) {
		ConfigurationProperty property = source
				.getConfigurationProperty(ConfigurationPropertyName.of(name));
		return (property != null) ? property.getValue() : null;
	}

}
