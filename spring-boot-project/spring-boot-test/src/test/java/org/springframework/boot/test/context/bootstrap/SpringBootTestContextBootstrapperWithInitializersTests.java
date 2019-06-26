

package org.springframework.boot.test.context.bootstrap;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.boot.test.context.bootstrap.SpringBootTestContextBootstrapperWithInitializersTests.CustomInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link SpringBootTestContextBootstrapper} with
 * {@link ApplicationContextInitializer}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ContextConfiguration(initializers = CustomInitializer.class)
public class SpringBootTestContextBootstrapperWithInitializersTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void foundConfiguration() {
		Object bean = this.context
				.getBean(SpringBootTestContextBootstrapperExampleConfig.class);
		assertThat(bean).isNotNull();
	}

	// gh-8483

	public static class CustomInitializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
		}

	}

}
