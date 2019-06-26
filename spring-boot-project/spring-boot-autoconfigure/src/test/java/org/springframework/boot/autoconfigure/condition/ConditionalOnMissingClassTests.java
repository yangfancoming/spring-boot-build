

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnMissingClass}.
 *
 * @author Dave Syer
 */
public class ConditionalOnMissingClassTests {

	private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@Test
	public void testVanillaOnClassCondition() {
		this.context.register(BasicConfiguration.class, FooConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("bar")).isFalse();
		assertThat(this.context.getBean("foo")).isEqualTo("foo");
	}

	@Test
	public void testMissingOnClassCondition() {
		this.context.register(MissingConfiguration.class, FooConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("bar")).isTrue();
		assertThat(this.context.getBean("foo")).isEqualTo("foo");
	}

	@Configuration
	@ConditionalOnMissingClass("org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClassTests")
	protected static class BasicConfiguration {

		@Bean
		public String bar() {
			return "bar";
		}

	}

	@Configuration
	@ConditionalOnMissingClass("FOO")
	protected static class MissingConfiguration {

		@Bean
		public String bar() {
			return "bar";
		}

	}

	@Configuration
	protected static class FooConfiguration {

		@Bean
		public String foo() {
			return "foo";
		}

	}

}
