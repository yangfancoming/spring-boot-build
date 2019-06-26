

package org.springframework.boot.jta.narayana;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.junit.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link NarayanaBeanFactoryPostProcessor}.
 *
 * @author Gytis Trikleris
 */
public class NarayanaBeanFactoryPostProcessorTests {

	private AnnotationConfigApplicationContext context;

	@Test
	public void setsDependsOn() {
		DefaultListableBeanFactory beanFactory = spy(new DefaultListableBeanFactory());
		this.context = new AnnotationConfigApplicationContext(beanFactory);
		this.context.register(Config.class);
		this.context.refresh();
		verify(beanFactory).registerDependentBean("narayanaTransactionManager",
				"dataSource");
		verify(beanFactory).registerDependentBean("narayanaTransactionManager",
				"connectionFactory");
		verify(beanFactory).registerDependentBean("narayanaRecoveryManagerBean",
				"dataSource");
		verify(beanFactory).registerDependentBean("narayanaRecoveryManagerBean",
				"connectionFactory");
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
		public TransactionManager narayanaTransactionManager() {
			return mock(TransactionManager.class);
		}

		@Bean
		public NarayanaRecoveryManagerBean narayanaRecoveryManagerBean() {
			return mock(NarayanaRecoveryManagerBean.class);
		}

		@Bean
		public static NarayanaBeanFactoryPostProcessor narayanaPostProcessor() {
			return new NarayanaBeanFactoryPostProcessor();
		}

	}

}
