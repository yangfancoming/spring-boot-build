

package org.springframework.boot.autoconfigure.jms;

import javax.jms.ConnectionFactory;
import javax.naming.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jndi.JndiPropertiesHidingClassLoader;
import org.springframework.boot.autoconfigure.jndi.TestableInitialContextFactory;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link JndiConnectionFactoryAutoConfiguration}.
 *
 * @author Stephane Nicoll
 */
public class JndiConnectionFactoryAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(JndiConnectionFactoryAutoConfiguration.class));

	private ClassLoader threadContextClassLoader;

	private String initialContextFactory;

	@Before
	public void setupJndi() {
		this.initialContextFactory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				TestableInitialContextFactory.class.getName());
		this.threadContextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(
				new JndiPropertiesHidingClassLoader(getClass().getClassLoader()));
	}

	@After
	public void cleanUp() {
		TestableInitialContextFactory.clearAll();
		if (this.initialContextFactory != null) {
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					this.initialContextFactory);
		}
		else {
			System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
		}
		Thread.currentThread().setContextClassLoader(this.threadContextClassLoader);
	}

	@Test
	public void detectNoAvailableCandidates() {
		this.contextRunner.run((context) -> assertThat(context)
				.doesNotHaveBean(ConnectionFactory.class));
	}

	@Test
	public void detectWithJmsXAConnectionFactory() {
		ConnectionFactory connectionFactory = configureConnectionFactory("java:/JmsXA");
		this.contextRunner.run(assertConnectionFactory(connectionFactory));
	}

	@Test
	public void detectWithXAConnectionFactory() {
		ConnectionFactory connectionFactory = configureConnectionFactory(
				"java:/XAConnectionFactory");
		this.contextRunner.run(assertConnectionFactory(connectionFactory));
	}

	@Test
	public void jndiNamePropertySet() {
		ConnectionFactory connectionFactory = configureConnectionFactory("myCF");
		this.contextRunner.withPropertyValues("spring.jms.jndi-name=myCF")
				.run(assertConnectionFactory(connectionFactory));
	}

	@Test
	public void jndiNamePropertySetWithWrongValue() {
		this.contextRunner.withPropertyValues("spring.jms.jndi-name=doesNotExistCF")
				.run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context).getFailure()
							.isInstanceOf(BeanCreationException.class)
							.hasMessageContaining("doesNotExistCF");
				});
	}

	private ContextConsumer<AssertableApplicationContext> assertConnectionFactory(
			ConnectionFactory connectionFactory) {
		return (context) -> {
			assertThat(context).hasSingleBean(ConnectionFactory.class);
			assertThat(context.getBean(ConnectionFactory.class))
					.isSameAs(connectionFactory);
		};
	}

	private ConnectionFactory configureConnectionFactory(String name) {
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
		TestableInitialContextFactory.bind(name, connectionFactory);
		return connectionFactory;
	}

}
