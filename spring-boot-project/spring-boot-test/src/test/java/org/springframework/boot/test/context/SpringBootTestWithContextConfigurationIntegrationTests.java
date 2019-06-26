

package org.springframework.boot.test.context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} configured with {@link ContextConfiguration}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@ContextConfiguration(classes = SpringBootTestWithContextConfigurationIntegrationTests.Config.class)
public class SpringBootTestWithContextConfigurationIntegrationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private ApplicationContext context;

	@Test
	public void injectsOnlyConfig() {
		assertThat(this.context.getBean(Config.class)).isNotNull();
		this.thrown.expect(NoSuchBeanDefinitionException.class);
		this.context.getBean(AdditionalConfig.class);
	}

	@Configuration
	static class Config {

	}

	@Configuration
	static class AdditionalConfig {

	}

}
