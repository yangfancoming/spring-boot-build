

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} with a custom config name
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.name=custom-config-name")
public class SpringBootTestCustomConfigNameTests {

	@Value("${test.foo}")
	private String foo;

	@Test
	public void propertyIsLoadedFromConfigFileWithCustomName() {
		assertThat(this.foo).isEqualTo("bar");
	}

	@Configuration
	static class TestConfiguration {

		public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

	}

}
