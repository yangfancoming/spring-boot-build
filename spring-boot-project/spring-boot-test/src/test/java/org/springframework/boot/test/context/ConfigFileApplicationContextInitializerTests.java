

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConfigFileApplicationContextInitializer}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@ContextConfiguration(classes = ConfigFileApplicationContextInitializerTests.Config.class, initializers = ConfigFileApplicationContextInitializer.class)
public class ConfigFileApplicationContextInitializerTests {

	@Autowired
	private Environment environment;

	@Test
	public void initializerPopulatesEnvironment() {
		assertThat(this.environment.getProperty("foo")).isEqualTo("bucket");
	}

	@Configuration
	public static class Config {

	}

}
