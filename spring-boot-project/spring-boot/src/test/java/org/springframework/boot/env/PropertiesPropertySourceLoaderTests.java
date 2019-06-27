

package org.springframework.boot.env;

import java.util.List;

import org.junit.Test;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PropertiesPropertySourceLoader}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class PropertiesPropertySourceLoaderTests {

	private PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

	@Test
	public void getFileExtensions() {
		assertThat(this.loader.getFileExtensions())
				.isEqualTo(new String[] { "properties", "xml" });
	}

	@Test
	public void loadProperties() throws Exception {
		List<PropertySource<?>> loaded = this.loader.load("test.properties",
				new ClassPathResource("test-properties.properties", getClass()));
		PropertySource<?> source = loaded.get(0);
		assertThat(source.getProperty("test")).isEqualTo("properties");
	}

	@Test
	public void loadXml() throws Exception {
		List<PropertySource<?>> loaded = this.loader.load("test.xml",
				new ClassPathResource("test-xml.xml", getClass()));
		PropertySource<?> source = loaded.get(0);
		assertThat(source.getProperty("test")).isEqualTo("xml");
	}

}
