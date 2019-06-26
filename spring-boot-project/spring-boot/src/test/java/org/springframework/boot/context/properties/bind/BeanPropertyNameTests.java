

package org.springframework.boot.context.properties.bind;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BeanPropertyName}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class BeanPropertyNameTests {

	@Test
	public void toDashedCaseShouldConvertValue() {
		assertThat(BeanPropertyName.toDashedForm("Foo")).isEqualTo("foo");
		assertThat(BeanPropertyName.toDashedForm("foo")).isEqualTo("foo");
		assertThat(BeanPropertyName.toDashedForm("fooBar")).isEqualTo("foo-bar");
		assertThat(BeanPropertyName.toDashedForm("foo_bar")).isEqualTo("foo-bar");
		assertThat(BeanPropertyName.toDashedForm("_foo_bar")).isEqualTo("-foo-bar");
		assertThat(BeanPropertyName.toDashedForm("foo_Bar")).isEqualTo("foo-bar");
	}

}
