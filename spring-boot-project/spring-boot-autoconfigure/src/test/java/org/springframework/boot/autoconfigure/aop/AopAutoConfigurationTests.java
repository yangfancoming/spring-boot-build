

package org.springframework.boot.autoconfigure.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AopAutoConfiguration}.
 *
 * @author Eberhard Wolff
 * @author Stephane Nicoll
 */
public class AopAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(AopAutoConfiguration.class));

	@Test
	public void aopDisabled() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.aop.auto:false").run((context) -> {
					TestAspect aspect = context.getBean(TestAspect.class);
					assertThat(aspect.isCalled()).isFalse();
					TestBean bean = context.getBean(TestBean.class);
					bean.foo();
					assertThat(aspect.isCalled()).isFalse();
				});
	}

	@Test
	public void aopWithDefaultSettings() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.run(proxyTargetClassEnabled());
	}

	@Test
	public void aopWithEnabledProxyTargetClass() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.aop.proxy-target-class:true")
				.run(proxyTargetClassEnabled());
	}

	@Test
	public void aopWithDisabledProxyTargetClass() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.aop.proxy-target-class:false")
				.run(proxyTargetClassDisabled());
	}

	@Test
	public void customConfigurationWithProxyTargetClassDefaultDoesNotDisableProxying() {
		this.contextRunner.withUserConfiguration(CustomTestConfiguration.class)
				.run(proxyTargetClassEnabled());
	}

	private ContextConsumer<AssertableApplicationContext> proxyTargetClassEnabled() {
		return (context) -> {
			TestAspect aspect = context.getBean(TestAspect.class);
			assertThat(aspect.isCalled()).isFalse();
			TestBean bean = context.getBean(TestBean.class);
			bean.foo();
			assertThat(aspect.isCalled()).isTrue();
		};
	}

	private ContextConsumer<AssertableApplicationContext> proxyTargetClassDisabled() {
		return (context) -> {
			TestAspect aspect = context.getBean(TestAspect.class);
			assertThat(aspect.isCalled()).isFalse();
			TestInterface bean = context.getBean(TestInterface.class);
			bean.foo();
			assertThat(aspect.isCalled()).isTrue();
			assertThat(context).doesNotHaveBean(TestBean.class);
		};
	}

	@EnableAspectJAutoProxy
	@Configuration
	@Import(TestConfiguration.class)
	protected static class CustomTestConfiguration {

	}

	@Configuration
	protected static class TestConfiguration {

		@Bean
		public TestAspect aspect() {
			return new TestAspect();
		}

		@Bean
		public TestInterface bean() {
			return new TestBean();
		}

	}

	protected static class TestBean implements TestInterface {

		@Override
		public void foo() {
		}

	}

	@Aspect
	protected static class TestAspect {

		private boolean called;

		public boolean isCalled() {
			return this.called;
		}

		@Before("execution(* foo(..))")
		public void before() {
			this.called = true;
		}

	}

	public interface TestInterface {

		void foo();

	}

}
