

package org.springframework.boot.test.mock.mockito;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.test.mock.mockito.example.ExampleService;
import org.springframework.boot.test.mock.mockito.example.FailingExampleService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link MockitoPostProcessor}. See also the integration tests.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class MockitoPostProcessorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void cannotMockMultipleBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		MockitoPostProcessor.register(context);
		context.register(MultipleBeans.class);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage(
				"Unable to register mock bean " + ExampleService.class.getName()
						+ " expected a single matching bean to replace "
						+ "but found [example1, example2]");
		context.refresh();
	}

	@Test
	public void cannotMockMultipleQualifiedBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		MockitoPostProcessor.register(context);
		context.register(MultipleQualifiedBeans.class);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage(
				"Unable to register mock bean " + ExampleService.class.getName()
						+ " expected a single matching bean to replace "
						+ "but found [example1, example3]");
		context.refresh();
	}

	@Test
	public void canMockBeanProducedByFactoryBeanWithObjectTypeAttribute() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		MockitoPostProcessor.register(context);
		RootBeanDefinition factoryBeanDefinition = new RootBeanDefinition(
				TestFactoryBean.class);
		factoryBeanDefinition.setAttribute("factoryBeanObjectType",
				SomeInterface.class.getName());
		context.registerBeanDefinition("beanToBeMocked", factoryBeanDefinition);
		context.register(MockedFactoryBean.class);
		context.refresh();
		assertThat(Mockito.mockingDetails(context.getBean("beanToBeMocked")).isMock())
				.isTrue();
	}

	@Configuration
	@MockBean(SomeInterface.class)
	static class MockedFactoryBean {

		@Bean
		public TestFactoryBean testFactoryBean() {
			return new TestFactoryBean();
		}

	}

	@Configuration
	@MockBean(ExampleService.class)
	static class MultipleBeans {

		@Bean
		public ExampleService example1() {
			return new FailingExampleService();
		}

		@Bean
		public ExampleService example2() {
			return new FailingExampleService();
		}

	}

	@Configuration
	static class MultipleQualifiedBeans {

		@MockBean(ExampleService.class)
		@Qualifier("test")
		private ExampleService mock;

		@Bean
		@Qualifier("test")
		public ExampleService example1() {
			return new FailingExampleService();
		}

		@Bean
		public ExampleService example2() {
			return new FailingExampleService();
		}

		@Bean
		@Qualifier("test")
		public ExampleService example3() {
			return new FailingExampleService();
		}

	}

	static class TestFactoryBean implements FactoryBean<Object> {

		@Override
		public Object getObject() {
			return new TestBean();
		}

		@Override
		public Class<?> getObjectType() {
			return null;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}

	}

	interface SomeInterface {

	}

	static class TestBean implements SomeInterface {

	}

}
