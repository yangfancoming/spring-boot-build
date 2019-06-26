

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} (detectDefaultConfigurationClasses).
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
public class SpringBootTestDefaultConfigurationTests {

	@Autowired
	private Config config;

	@Test
	public void nestedConfigClasses() {
		assertThat(this.config).isNotNull();
	}

	@Configuration
	protected static class Config {

	}

}
