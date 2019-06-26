

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} (detectDefaultConfigurationClasses).
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@ContextConfiguration(locations = "classpath:test.groovy")
public class SpringBootTestGroovyConfigurationTests {

	@Autowired
	private String foo;

	@Test
	public void groovyConfigLoaded() {
		assertThat(this.foo).isNotNull();
	}

}
