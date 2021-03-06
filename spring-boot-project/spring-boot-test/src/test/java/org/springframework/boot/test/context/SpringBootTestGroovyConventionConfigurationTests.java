

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} finding groovy config.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringBootTestGroovyConventionConfigurationTests {

	@Autowired
	private String foo;

	@Test
	public void groovyConfigLoaded() {
		assertThat(this.foo).isEqualTo("World");
	}

}
