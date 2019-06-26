

package org.springframework.boot.context.properties.source;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConfigurationPropertyNameAliases}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class ConfigurationPropertyNameAliasesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWithStringWhenNullNameShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Name must not be null");
		new ConfigurationPropertyNameAliases((String) null);
	}

	@Test
	public void createWithStringShouldAddMapping() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(
				"foo", "bar", "baz");
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"),
						ConfigurationPropertyName.of("baz"));
	}

	@Test
	public void createWithNameShouldAddMapping() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(
				ConfigurationPropertyName.of("foo"), ConfigurationPropertyName.of("bar"),
				ConfigurationPropertyName.of("baz"));
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"),
						ConfigurationPropertyName.of("baz"));
	}

	@Test
	public void addAliasesFromStringShouldAddMapping() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("foo", "bar", "baz");
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"),
						ConfigurationPropertyName.of("baz"));
	}

	@Test
	public void addAliasesFromNameShouldAddMapping() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases(ConfigurationPropertyName.of("foo"),
				ConfigurationPropertyName.of("bar"), ConfigurationPropertyName.of("baz"));
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"),
						ConfigurationPropertyName.of("baz"));
	}

	@Test
	public void addWhenHasExistingShouldAddAdditionalMappings() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("foo", "bar");
		aliases.addAliases("foo", "baz");
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"),
						ConfigurationPropertyName.of("baz"));
	}

	@Test
	public void getAliasesWhenNotMappedShouldReturnEmptyList() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo"))).isEmpty();
	}

	@Test
	public void getAliasesWhenMappedShouldReturnMapping() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("foo", "bar");
		assertThat(aliases.getAliases(ConfigurationPropertyName.of("foo")))
				.containsExactly(ConfigurationPropertyName.of("bar"));
	}

	@Test
	public void getNameForAliasWhenHasMappingShouldReturnName() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("foo", "bar");
		aliases.addAliases("foo", "baz");
		assertThat((Object) aliases.getNameForAlias(ConfigurationPropertyName.of("bar")))
				.isEqualTo(ConfigurationPropertyName.of("foo"));
		assertThat((Object) aliases.getNameForAlias(ConfigurationPropertyName.of("baz")))
				.isEqualTo(ConfigurationPropertyName.of("foo"));
	}

	@Test
	public void getNameForAliasWhenNotMappedShouldReturnNull() {
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("foo", "bar");
		assertThat((Object) aliases.getNameForAlias(ConfigurationPropertyName.of("baz")))
				.isNull();
	}

}
