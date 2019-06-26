

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;

import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnClass}.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
public class ConditionalOnClassTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void testVanillaOnClassCondition() {
		this.contextRunner
				.withUserConfiguration(BasicConfiguration.class, FooConfiguration.class)
				.run(this::hasBarBean);
	}

	@Test
	public void testMissingOnClassCondition() {
		this.contextRunner
				.withUserConfiguration(MissingConfiguration.class, FooConfiguration.class)
				.run((context) -> {
					assertThat(context).doesNotHaveBean("bar");
					assertThat(context).hasBean("foo");
					assertThat(context.getBean("foo")).isEqualTo("foo");
				});
	}

	@Test
	public void testOnClassConditionWithXml() {
		this.contextRunner
				.withUserConfiguration(BasicConfiguration.class, XmlConfiguration.class)
				.run(this::hasBarBean);
	}

	@Test
	public void testOnClassConditionWithCombinedXml() {
		this.contextRunner.withUserConfiguration(CombinedXmlConfiguration.class)
				.run(this::hasBarBean);
	}

	private void hasBarBean(AssertableApplicationContext context) {
		assertThat(context).hasBean("bar");
		assertThat(context.getBean("bar")).isEqualTo("bar");
	}

	@Configuration
	@ConditionalOnClass(ConditionalOnClassTests.class)
	protected static class BasicConfiguration {

		@Bean
		public String bar() {
			return "bar";
		}

	}

	@Configuration
	@ConditionalOnClass(name = "FOO")
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

	@Configuration
	@ImportResource("org/springframework/boot/autoconfigure/condition/foo.xml")
	protected static class XmlConfiguration {

	}

	@Configuration
	@Import(BasicConfiguration.class)
	@ImportResource("org/springframework/boot/autoconfigure/condition/foo.xml")
	protected static class CombinedXmlConfiguration {

	}

}
