

package org.springframework.boot.test.autoconfigure.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.ExampleEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@code ImportsContextCustomizerFactory} when used with
 * {@link ImportAutoConfiguration}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class ImportsContextCustomizerFactoryWithAutoConfigurationTests {

	static ApplicationContext contextFromTest;

	@Test
	public void testClassesThatHaveSameAnnotationsShareAContext()
			throws InitializationError {
		RunNotifier notifier = new RunNotifier();
		new SpringJUnit4ClassRunner(DataJpaTest1.class).run(notifier);
		ApplicationContext test1Context = contextFromTest;
		new SpringJUnit4ClassRunner(DataJpaTest3.class).run(notifier);
		ApplicationContext test2Context = contextFromTest;
		assertThat(test1Context).isSameAs(test2Context);
	}

	@Test
	public void testClassesThatOnlyHaveDifferingUnrelatedAnnotationsShareAContext()
			throws InitializationError {
		RunNotifier notifier = new RunNotifier();
		new SpringJUnit4ClassRunner(DataJpaTest1.class).run(notifier);
		ApplicationContext test1Context = contextFromTest;
		new SpringJUnit4ClassRunner(DataJpaTest2.class).run(notifier);
		ApplicationContext test2Context = contextFromTest;
		assertThat(test1Context).isSameAs(test2Context);
	}

	@Test
	public void testClassesThatOnlyHaveDifferingPropertyMappedAnnotationAttributesDoNotShareAContext()
			throws InitializationError {
		RunNotifier notifier = new RunNotifier();
		new SpringJUnit4ClassRunner(DataJpaTest1.class).run(notifier);
		ApplicationContext test1Context = contextFromTest;
		new SpringJUnit4ClassRunner(DataJpaTest4.class).run(notifier);
		ApplicationContext test2Context = contextFromTest;
		assertThat(test1Context).isNotSameAs(test2Context);
	}

	@DataJpaTest
	@ContextConfiguration(classes = EmptyConfig.class)
	@Unrelated1
	public static class DataJpaTest1 {

		@Autowired
		private ApplicationContext context;

		@Test
		public void test() {
			contextFromTest = this.context;
		}

	}

	@DataJpaTest
	@ContextConfiguration(classes = EmptyConfig.class)
	@Unrelated2
	public static class DataJpaTest2 {

		@Autowired
		private ApplicationContext context;

		@Test
		public void test() {
			contextFromTest = this.context;
		}

	}

	@DataJpaTest
	@ContextConfiguration(classes = EmptyConfig.class)
	@Unrelated1
	public static class DataJpaTest3 {

		@Autowired
		private ApplicationContext context;

		@Test
		public void test() {
			contextFromTest = this.context;
		}

	}

	@DataJpaTest(showSql = false)
	@ContextConfiguration(classes = EmptyConfig.class)
	@Unrelated1
	public static class DataJpaTest4 {

		@Autowired
		private ApplicationContext context;

		@Test
		public void test() {
			contextFromTest = this.context;
		}

	}

	@Retention(RetentionPolicy.RUNTIME)
	static @interface Unrelated1 {

	}

	@Retention(RetentionPolicy.RUNTIME)
	static @interface Unrelated2 {

	}

	@Configuration
	@EntityScan(basePackageClasses = ExampleEntity.class)
	@AutoConfigurationPackage
	static class EmptyConfig {

	}

}
