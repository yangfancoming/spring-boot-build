

package org.springframework.boot.context.properties.source;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MapConfigurationPropertySource}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class MapConfigurationPropertySourceTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenMapIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Map must not be null");
		new MapConfigurationPropertySource(null);
	}

	@Test
	public void createWhenMapHasEntriesShouldAdaptMap() {
		Map<Object, Object> map = new LinkedHashMap<>();
		map.put("foo.BAR", "spring");
		map.put(ConfigurationPropertyName.of("foo.baz"), "boot");
		MapConfigurationPropertySource source = new MapConfigurationPropertySource(map);
		assertThat(getValue(source, "foo.bar")).isEqualTo("spring");
		assertThat(getValue(source, "foo.baz")).isEqualTo("boot");
	}

	@Test
	public void putAllWhenMapIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Map must not be null");
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.putAll(null);
	}

	@Test
	public void putAllShouldPutEntries() {
		Map<Object, Object> map = new LinkedHashMap<>();
		map.put("foo.BAR", "spring");
		map.put("foo.baz", "boot");
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.putAll(map);
		assertThat(getValue(source, "foo.bar")).isEqualTo("spring");
		assertThat(getValue(source, "foo.baz")).isEqualTo("boot");
	}

	@Test
	public void putShouldPutEntry() {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.put("foo.bar", "baz");
		assertThat(getValue(source, "foo.bar")).isEqualTo("baz");
	}

	@Test
	public void getConfigurationPropertyShouldGetFromMemory() {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.put("foo.bar", "baz");
		assertThat(getValue(source, "foo.bar")).isEqualTo("baz");
		source.put("foo.bar", "big");
		assertThat(getValue(source, "foo.bar")).isEqualTo("big");
	}

	@Test
	public void iteratorShouldGetFromMemory() {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.put("foo.BAR", "spring");
		source.put("foo.baz", "boot");
		assertThat(source.iterator()).containsExactly(
				ConfigurationPropertyName.of("foo.bar"),
				ConfigurationPropertyName.of("foo.baz"));
	}

	@Test
	public void streamShouldGetFromMemory() {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.put("foo.BAR", "spring");
		source.put("foo.baz", "boot");
		assertThat(source.stream()).containsExactly(
				ConfigurationPropertyName.of("foo.bar"),
				ConfigurationPropertyName.of("foo.baz"));

	}

	private Object getValue(ConfigurationPropertySource source, String name) {
		ConfigurationProperty property = source
				.getConfigurationProperty(ConfigurationPropertyName.of(name));
		return (property != null) ? property.getValue() : null;
	}

}
