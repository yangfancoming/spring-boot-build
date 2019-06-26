

package org.springframework.boot.autoconfigure.condition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Tests for {@link SpringBootCondition}.
 *
 * @author Phillip Webb
 */
@SuppressWarnings("resource")
public class SpringBootConditionTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void sensibleClassException() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage(
				"Error processing condition on " + ErrorOnClass.class.getName());
		new AnnotationConfigApplicationContext(ErrorOnClass.class);
	}

	@Test
	public void sensibleMethodException() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Error processing condition on "
				+ ErrorOnMethod.class.getName() + ".myBean");
		new AnnotationConfigApplicationContext(ErrorOnMethod.class);
	}

	@Configuration
	@Conditional(AlwaysThrowsCondition.class)
	public static class ErrorOnClass {

	}

	@Configuration
	public static class ErrorOnMethod {

		@Bean
		@Conditional(AlwaysThrowsCondition.class)
		public String myBean() {
			return "bean";
		}

	}

	public static class AlwaysThrowsCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			throw new RuntimeException("Oh no!");
		}

	}

}
