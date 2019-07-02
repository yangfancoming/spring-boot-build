

package org.springframework.boot.test.autoconfigure;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OverrideAutoConfiguration} when {@code enabled} is
 * {@code true}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@OverrideAutoConfiguration(enabled = true)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ImportAutoConfiguration(ExampleTestConfig.class)
public class OverrideAutoConfigurationEnabledTrueIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void autoConfiguredContext() {
		ApplicationContext context = this.context;
		assertThat(context.getBean(ExampleSpringBootApplication.class)).isNotNull();
		assertThat(context.getBean(ConfigurationPropertiesBindingPostProcessor.class))
				.isNotNull();
	}

}
