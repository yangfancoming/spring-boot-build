

package org.springframework.boot.test.autoconfigure;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
 * {@code false}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@OverrideAutoConfiguration(enabled = false)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ImportAutoConfiguration(ExampleTestConfig.class)
public class OverrideAutoConfigurationEnabledFalseIntegrationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private ApplicationContext context;

	@Test
	public void disabledAutoConfiguration() {
		ApplicationContext context = this.context;
		assertThat(context.getBean(ExampleTestConfig.class)).isNotNull();
		this.thrown.expect(NoSuchBeanDefinitionException.class);
		context.getBean(ConfigurationPropertiesBindingPostProcessor.class);
	}

}
