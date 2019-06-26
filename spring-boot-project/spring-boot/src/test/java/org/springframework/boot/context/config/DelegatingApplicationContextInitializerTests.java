

package org.springframework.boot.context.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DelegatingApplicationContextInitializer}.
 *
 * @author Phillip Webb
 */
public class DelegatingApplicationContextInitializerTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final DelegatingApplicationContextInitializer initializer = new DelegatingApplicationContextInitializer();

	@Test
	public void orderedInitialize() {
		StaticApplicationContext context = new StaticApplicationContext();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
				"context.initializer.classes=" + MockInitB.class.getName() + ","
						+ MockInitA.class.getName());
		this.initializer.initialize(context);
		assertThat(context.getBeanFactory().getSingleton("a")).isEqualTo("a");
		assertThat(context.getBeanFactory().getSingleton("b")).isEqualTo("b");
	}

	@Test
	public void noInitializers() {
		StaticApplicationContext context = new StaticApplicationContext();
		this.initializer.initialize(context);
	}

	@Test
	public void emptyInitializers() {
		StaticApplicationContext context = new StaticApplicationContext();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
				"context.initializer.classes:");
		this.initializer.initialize(context);
	}

	@Test
	public void noSuchInitializerClass() {
		StaticApplicationContext context = new StaticApplicationContext();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
				"context.initializer.classes=missing.madeup.class");
		this.thrown.expect(ApplicationContextException.class);
		this.initializer.initialize(context);
	}

	@Test
	public void notAnInitializerClass() {
		StaticApplicationContext context = new StaticApplicationContext();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
				"context.initializer.classes=" + Object.class.getName());
		this.thrown.expect(IllegalArgumentException.class);
		this.initializer.initialize(context);
	}

	@Test
	public void genericNotSuitable() {
		StaticApplicationContext context = new StaticApplicationContext();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
				"context.initializer.classes=" + NotSuitableInit.class.getName());
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("generic parameter");
		this.initializer.initialize(context);
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	private static class MockInitA
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			applicationContext.getBeanFactory().registerSingleton("a", "a");
		}

	}

	@Order(Ordered.LOWEST_PRECEDENCE)
	private static class MockInitB
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			assertThat(applicationContext.getBeanFactory().getSingleton("a"))
					.isEqualTo("a");
			applicationContext.getBeanFactory().registerSingleton("b", "b");
		}

	}

	private static class NotSuitableInit
			implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

		@Override
		public void initialize(ConfigurableWebApplicationContext applicationContext) {
		}

	}

}
