

package org.springframework.boot.context.properties.bind;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.util.PropertyPlaceholderHelper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PropertySourcesPlaceholdersResolver}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class PropertySourcesPlaceholdersResolverTests {

	private PropertySourcesPlaceholdersResolver resolver;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void placeholderResolverIfEnvironmentNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Environment must not be null");
		new PropertySourcesPlaceholdersResolver((Environment) null);
	}

	@Test
	public void resolveIfPlaceholderPresentResolvesProperty() {
		MutablePropertySources sources = getPropertySources();
		this.resolver = new PropertySourcesPlaceholdersResolver(sources);
		Object resolved = this.resolver.resolvePlaceholders("${FOO}");
		assertThat(resolved).isEqualTo("hello world");
	}

	@Test
	public void resolveIfPlaceholderAbsentUsesDefault() {
		this.resolver = new PropertySourcesPlaceholdersResolver((PropertySources) null);
		Object resolved = this.resolver.resolvePlaceholders("${FOO:bar}");
		assertThat(resolved).isEqualTo("bar");
	}

	@Test
	public void resolveIfPlaceholderAbsentAndNoDefaultUsesPlaceholder() {
		this.resolver = new PropertySourcesPlaceholdersResolver((PropertySources) null);
		Object resolved = this.resolver.resolvePlaceholders("${FOO}");
		assertThat(resolved).isEqualTo("${FOO}");
	}

	@Test
	public void resolveIfHelperPresentShouldUseIt() {
		MutablePropertySources sources = getPropertySources();
		TestPropertyPlaceholderHelper helper = new TestPropertyPlaceholderHelper("$<",
				">");
		this.resolver = new PropertySourcesPlaceholdersResolver(sources, helper);
		Object resolved = this.resolver.resolvePlaceholders("$<FOO>");
		assertThat(resolved).isEqualTo("hello world");
	}

	private MutablePropertySources getPropertySources() {
		MutablePropertySources sources = new MutablePropertySources();
		Map<String, Object> source = new HashMap<>();
		source.put("FOO", "hello world");
		sources.addFirst(new MapPropertySource("test", source));
		return sources;
	}

	static class TestPropertyPlaceholderHelper extends PropertyPlaceholderHelper {

		TestPropertyPlaceholderHelper(String placeholderPrefix,
				String placeholderSuffix) {
			super(placeholderPrefix, placeholderSuffix);
		}

	}

}
