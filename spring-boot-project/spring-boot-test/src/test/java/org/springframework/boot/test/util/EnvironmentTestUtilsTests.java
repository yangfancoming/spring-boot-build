

package org.springframework.boot.test.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EnvironmentTestUtils}.
 *
 * @author Stephane Nicoll
 */
@Deprecated
public class EnvironmentTestUtilsTests {

	private final ConfigurableEnvironment environment = new StandardEnvironment();

	@Test
	public void addSimplePairEqual() {
		testAddSimplePair("my.foo", "bar", "=");
	}

	@Test
	public void addSimplePairColon() {
		testAddSimplePair("my.foo", "bar", ":");
	}

	@Test
	public void addSimplePairEqualWithEqualInValue() {
		testAddSimplePair("my.foo", "b=ar", "=");
	}

	@Test
	public void addSimplePairEqualWithColonInValue() {
		testAddSimplePair("my.foo", "b:ar", "=");
	}

	@Test
	public void addSimplePairColonWithColonInValue() {
		testAddSimplePair("my.foo", "b:ar", ":");
	}

	@Test
	public void addSimplePairColonWithEqualInValue() {
		testAddSimplePair("my.foo", "b=ar", ":");
	}

	@Test
	public void addPairNoValue() {
		String propertyName = "my.foo+bar";
		assertThat(this.environment.containsProperty(propertyName)).isFalse();
		EnvironmentTestUtils.addEnvironment(this.environment, propertyName);
		assertThat(this.environment.containsProperty(propertyName)).isTrue();
		assertThat(this.environment.getProperty(propertyName)).isEqualTo("");
	}

	private void testAddSimplePair(String key, String value, String delimiter) {
		assertThat(this.environment.containsProperty(key)).isFalse();
		EnvironmentTestUtils.addEnvironment(this.environment, key + delimiter + value);
		assertThat(this.environment.getProperty(key)).isEqualTo(value);
	}

	@Test
	public void testConfigHasHigherPrecedence() {
		Map<String, Object> map = new HashMap<>();
		map.put("my.foo", "bar");
		MapPropertySource source = new MapPropertySource("sample", map);
		this.environment.getPropertySources().addFirst(source);
		assertThat(this.environment.getProperty("my.foo")).isEqualTo("bar");
		EnvironmentTestUtils.addEnvironment(this.environment, "my.foo=bar2");
		assertThat(this.environment.getProperty("my.foo")).isEqualTo("bar2");
	}

}
