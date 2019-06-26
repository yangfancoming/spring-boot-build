

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestMixedConfigurationTests.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest}.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@ContextConfiguration(classes = Config.class, locations = "classpath:test.groovy")
public class SpringBootTestMixedConfigurationTests {

	@Autowired
	private String foo;

	@Autowired
	private Config config;

	@Test
	public void mixedConfigClasses() {
		assertThat(this.foo).isNotNull();
		assertThat(this.config).isNotNull();
	}

	@Configuration
	protected static class Config {

	}

}
