

package org.springframework.boot.test.autoconfigure.properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link PropertyMapping @PropertyMapping} annotations.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@ExampleMapping(exampleProperty = "abc")
public class PropertyMappingTests {

	@Autowired
	private Environment environment;

	@Test
	public void hasProperty() {
		assertThat(this.environment.getProperty("example-property")).isEqualTo("abc");
	}

}
