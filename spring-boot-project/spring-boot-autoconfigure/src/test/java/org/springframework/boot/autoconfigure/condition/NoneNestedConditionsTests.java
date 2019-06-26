

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NoneNestedConditions}.
 */
public class NoneNestedConditionsTests {

	@Test
	public void neither() {
		AnnotationConfigApplicationContext context = load(Config.class);
		assertThat(context.containsBean("myBean")).isTrue();
		context.close();
	}

	@Test
	public void propertyA() {
		AnnotationConfigApplicationContext context = load(Config.class, "a:a");
		assertThat(context.containsBean("myBean")).isFalse();
		context.close();
	}

	@Test
	public void propertyB() {
		AnnotationConfigApplicationContext context = load(Config.class, "b:b");
		assertThat(context.containsBean("myBean")).isFalse();
		context.close();
	}

	@Test
	public void both() {
		AnnotationConfigApplicationContext context = load(Config.class, "a:a", "b:b");
		assertThat(context.containsBean("myBean")).isFalse();
		context.close();
	}

	private AnnotationConfigApplicationContext load(Class<?> config, String... env) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(env).applyTo(context);
		context.register(config);
		context.refresh();
		return context;
	}

	@Configuration
	@Conditional(NeitherPropertyANorPropertyBCondition.class)
	public static class Config {

		@Bean
		public String myBean() {
			return "myBean";
		}

	}

	static class NeitherPropertyANorPropertyBCondition extends NoneNestedConditions {

		NeitherPropertyANorPropertyBCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty("a")
		static class HasPropertyA {

		}

		@ConditionalOnProperty("b")
		static class HasPropertyB {

		}

		@Conditional(NonSpringBootCondition.class)
		static class SubClassC {

		}

	}

	static class NonSpringBootCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return false;
		}

	}

}
