

package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.hsqldb.jdbc.pool.JDBCXADataSource;
import org.junit.Test;

import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link XADataSourceAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class XADataSourceAutoConfigurationTests {

	@Test
	public void wrapExistingXaDataSource() {
		ApplicationContext context = createContext(WrapExisting.class);
		context.getBean(DataSource.class);
		XADataSource source = context.getBean(XADataSource.class);
		MockXADataSourceWrapper wrapper = context.getBean(MockXADataSourceWrapper.class);
		assertThat(wrapper.getXaDataSource()).isEqualTo(source);
	}

	@Test
	public void createFromUrl() {
		ApplicationContext context = createContext(FromProperties.class,
				"spring.datasource.url:jdbc:hsqldb:mem:test",
				"spring.datasource.username:un");
		context.getBean(DataSource.class);
		MockXADataSourceWrapper wrapper = context.getBean(MockXADataSourceWrapper.class);
		JDBCXADataSource dataSource = (JDBCXADataSource) wrapper.getXaDataSource();
		assertThat(dataSource).isNotNull();
		assertThat(dataSource.getUrl()).isEqualTo("jdbc:hsqldb:mem:test");
		assertThat(dataSource.getUser()).isEqualTo("un");
	}

	@Test
	public void createFromClass() throws Exception {
		ApplicationContext context = createContext(FromProperties.class,
				"spring.datasource.xa.data-source-class-name:org.hsqldb.jdbc.pool.JDBCXADataSource",
				"spring.datasource.xa.properties.login-timeout:123");
		context.getBean(DataSource.class);
		MockXADataSourceWrapper wrapper = context.getBean(MockXADataSourceWrapper.class);
		JDBCXADataSource dataSource = (JDBCXADataSource) wrapper.getXaDataSource();
		assertThat(dataSource).isNotNull();
		assertThat(dataSource.getLoginTimeout()).isEqualTo(123);
	}

	private ApplicationContext createContext(Class<?> configuration, String... env) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(env).applyTo(context);
		context.register(configuration, XADataSourceAutoConfiguration.class);
		context.refresh();
		return context;
	}

	@Configuration
	static class WrapExisting {

		@Bean
		public MockXADataSourceWrapper wrapper() {
			return new MockXADataSourceWrapper();
		}

		@Bean
		public XADataSource xaDataSource() {
			return mock(XADataSource.class);
		}

	}

	@Configuration
	static class FromProperties {

		@Bean
		public MockXADataSourceWrapper wrapper() {
			return new MockXADataSourceWrapper();
		}

	}

	private static class MockXADataSourceWrapper implements XADataSourceWrapper {

		private XADataSource dataSource;

		@Override
		public DataSource wrapDataSource(XADataSource dataSource) {
			this.dataSource = dataSource;
			return mock(DataSource.class);
		}

		public XADataSource getXaDataSource() {
			return this.dataSource;
		}

	}

}
