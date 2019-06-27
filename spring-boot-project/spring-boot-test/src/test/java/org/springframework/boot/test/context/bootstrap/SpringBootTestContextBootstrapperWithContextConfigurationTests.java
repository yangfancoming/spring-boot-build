

package org.springframework.boot.test.context.bootstrap;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTestContextBootstrapper} + {@code @ContextConfiguration} (in
 * its own package so we can test detection).
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ContextConfiguration
public class SpringBootTestContextBootstrapperWithContextConfigurationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SpringBootTestContextBootstrapperExampleConfig config;

	@Test
	public void findConfigAutomatically() {
		assertThat(this.config).isNotNull();
	}

	@Test
	public void contextWasCreatedViaSpringApplication() {
		assertThat(this.context.getId()).startsWith("application");
	}

}
