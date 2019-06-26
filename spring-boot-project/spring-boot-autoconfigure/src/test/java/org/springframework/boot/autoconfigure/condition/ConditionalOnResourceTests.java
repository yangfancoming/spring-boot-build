

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnResource}.
 *
 * @author Dave Syer
 */
public class ConditionalOnResourceTests {

	private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@Test
	public void testResourceExists() {
		this.context.register(BasicConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("foo")).isTrue();
		assertThat(this.context.getBean("foo")).isEqualTo("foo");
	}

	@Test
	public void testResourceExistsWithPlaceholder() {
		TestPropertyValues.of("schema=schema.sql").applyTo(this.context);
		this.context.register(PlaceholderConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("foo")).isTrue();
		assertThat(this.context.getBean("foo")).isEqualTo("foo");
	}

	@Test
	public void testResourceNotExists() {
		this.context.register(MissingConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Configuration
	@ConditionalOnResource(resources = "foo")
	protected static class MissingConfiguration {

		@Bean
		public String bar() {
			return "bar";
		}

	}

	@Configuration
	@ConditionalOnResource(resources = "schema.sql")
	protected static class BasicConfiguration {

		@Bean
		public String foo() {
			return "foo";
		}

	}

	@Configuration
	@ConditionalOnResource(resources = "${schema}")
	protected static class PlaceholderConfiguration {

		@Bean
		public String foo() {
			return "foo";
		}

	}

}
