

package org.springframework.boot.jta.bitronix;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import bitronix.tm.BitronixTransactionManager;
import org.junit.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link BitronixDependentBeanFactoryPostProcessor}.
 *
 * @author Phillip Webb
 */
public class BitronixDependentBeanFactoryPostProcessorTests {

	private AnnotationConfigApplicationContext context;

	@Test
	public void setsDependsOn() {
		DefaultListableBeanFactory beanFactory = spy(new DefaultListableBeanFactory());
		this.context = new AnnotationConfigApplicationContext(beanFactory);
		this.context.register(Config.class);
		this.context.refresh();
		String name = "bitronixTransactionManager";
		verify(beanFactory).registerDependentBean(name, "dataSource");
		verify(beanFactory).registerDependentBean(name, "connectionFactory");
		this.context.close();
	}

	@Configuration
	static class Config {

		@Bean
		public DataSource dataSource() {
			return mock(DataSource.class);
		}

		@Bean
		public ConnectionFactory connectionFactory() {
			return mock(ConnectionFactory.class);
		}

		@Bean
		public BitronixTransactionManager bitronixTransactionManager() {
			return mock(BitronixTransactionManager.class);
		}

		@Bean
		public static BitronixDependentBeanFactoryPostProcessor bitronixPostProcessor() {
			return new BitronixDependentBeanFactoryPostProcessor();
		}

	}

}
