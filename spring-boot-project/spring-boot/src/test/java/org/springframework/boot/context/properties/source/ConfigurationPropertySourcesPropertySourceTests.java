

package org.springframework.boot.context.properties.source;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConfigurationPropertySourcesPropertySource}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class ConfigurationPropertySourcesPropertySourceTests {

	private List<ConfigurationPropertySource> configurationSources = new ArrayList<>();

	private ConfigurationPropertySourcesPropertySource propertySource = new ConfigurationPropertySourcesPropertySource(
			"test", this.configurationSources);

	@Test
	public void getPropertyShouldReturnValue() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz"));
		assertThat(this.propertySource.getProperty("foo.bar")).isEqualTo("baz");
	}

	@Test
	public void getPropertyWhenNameIsNotValidShouldReturnNull() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz"));
		assertThat(this.propertySource.getProperty("FOO.B-A-R")).isNull();
		assertThat(this.propertySource.getProperty("FOO.B A R")).isNull();
		assertThat(this.propertySource.getProperty(".foo.bar")).isNull();
	}

	@Test
	public void getPropertyWhenMultipleShouldReturnFirst() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz"));
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "bill"));
		assertThat(this.propertySource.getProperty("foo.bar")).isEqualTo("baz");
	}

	@Test
	public void getPropertyWhenNoneShouldReturnFirst() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz"));
		assertThat(this.propertySource.getProperty("foo.foo")).isNull();
	}

	@Test
	public void getPropertyOriginShouldReturnOrigin() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz", "line1"));
		assertThat(this.propertySource.getOrigin("foo.bar").toString())
				.isEqualTo("line1");
	}

	@Test
	public void getPropertyOriginWhenMissingShouldReturnNull() {
		this.configurationSources
				.add(new MockConfigurationPropertySource("foo.bar", "baz", "line1"));
		assertThat(this.propertySource.getOrigin("foo.foo")).isNull();
	}

	@Test
	public void getNameShouldReturnName() {
		assertThat(this.propertySource.getName()).isEqualTo("test");
	}

	@Test
	public void getSourceShouldReturnSource() {
		assertThat(this.propertySource.getSource()).isSameAs(this.configurationSources);
	}

}
