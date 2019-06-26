

package org.springframework.boot.autoconfigure;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages.Registrar;
import org.springframework.boot.autoconfigure.packagestest.one.FirstConfiguration;
import org.springframework.boot.autoconfigure.packagestest.two.SecondConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AutoConfigurationPackages}.
 *
 * @author Phillip Webb
 * @author Oliver Gierke
 */
@SuppressWarnings("resource")
public class AutoConfigurationPackagesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void setAndGet() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ConfigWithRegistrar.class);
		assertThat(AutoConfigurationPackages.get(context.getBeanFactory()))
				.containsExactly(getClass().getPackage().getName());
	}

	@Test
	public void getWithoutSet() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				EmptyConfig.class);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage(
				"Unable to retrieve @EnableAutoConfiguration base packages");
		AutoConfigurationPackages.get(context.getBeanFactory());
	}

	@Test
	public void detectsMultipleAutoConfigurationPackages() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				FirstConfiguration.class, SecondConfiguration.class);
		List<String> packages = AutoConfigurationPackages.get(context.getBeanFactory());
		Package package1 = FirstConfiguration.class.getPackage();
		Package package2 = SecondConfiguration.class.getPackage();
		assertThat(packages).containsOnly(package1.getName(), package2.getName());
	}

	@Configuration
	@Import(AutoConfigurationPackages.Registrar.class)
	static class ConfigWithRegistrar {

	}

	@Configuration
	static class EmptyConfig {

	}

	/**
	 * Test helper to allow {@link Registrar} to be referenced from other packages.
	 */
	public static class TestRegistrar extends Registrar {

	}

}
