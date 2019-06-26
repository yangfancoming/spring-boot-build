

package org.springframework.boot.autoconfigure.condition;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ResourceCondition}.
 *
 * @author Stephane Nicoll
 */
public class ResourceConditionTests {

	private ConfigurableApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void defaultResourceAndNoExplicitKey() {
		load(DefaultLocationConfiguration.class);
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void unknownDefaultLocationAndNoExplicitKey() {
		load(UnknownDefaultLocationConfiguration.class);
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	public void unknownDefaultLocationAndExplicitKeyToResource() {
		load(UnknownDefaultLocationConfiguration.class,
				"spring.foo.test.config=logging.properties");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(environment).applyTo(applicationContext);
		applicationContext.register(config);
		applicationContext.refresh();
		this.context = applicationContext;
	}

	@Configuration
	@Conditional(DefaultLocationResourceCondition.class)
	static class DefaultLocationConfiguration {

		@Bean
		public String foo() {
			return "foo";
		}

	}

	@Configuration
	@Conditional(UnknownDefaultLocationResourceCondition.class)
	static class UnknownDefaultLocationConfiguration {

		@Bean
		public String foo() {
			return "foo";
		}

	}

	private static class DefaultLocationResourceCondition extends ResourceCondition {

		DefaultLocationResourceCondition() {
			super("test", "spring.foo.test.config", "classpath:/logging.properties");
		}

	}

	private static class UnknownDefaultLocationResourceCondition
			extends ResourceCondition {

		UnknownDefaultLocationResourceCondition() {
			super("test", "spring.foo.test.config",
					"classpath:/this-file-does-not-exist.xml");
		}

	}

}
