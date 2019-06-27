

package org.springframework.boot.autoconfigure.jdbc;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DataSourceAutoConfiguration} with Hikari.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
public class HikariDataSourceConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
			.withPropertyValues("spring.datasource.initialization-mode=never",
					"spring.datasource.type=" + HikariDataSource.class.getName());

	@Test
	public void testDataSourceExists() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBeansOfType(DataSource.class)).hasSize(1);
			assertThat(context.getBeansOfType(HikariDataSource.class)).hasSize(1);
		});
	}

	@Test
	public void testDataSourcePropertiesOverridden() {
		this.contextRunner.withPropertyValues(
				"spring.datasource.hikari.jdbc-url=jdbc:foo//bar/spam",
				"spring.datasource.hikari.max-lifetime=1234").run((context) -> {
					HikariDataSource ds = context.getBean(HikariDataSource.class);
					assertThat(ds.getJdbcUrl()).isEqualTo("jdbc:foo//bar/spam");
					assertThat(ds.getMaxLifetime()).isEqualTo(1234);
				});
		// TODO: test JDBC4 isValid()
	}

	@Test
	public void testDataSourceGenericPropertiesOverridden() {
		this.contextRunner
				.withPropertyValues("spring.datasource.hikari.data-source-properties"
						+ ".dataSourceClassName=org.h2.JDBCDataSource")
				.run((context) -> {
					HikariDataSource ds = context.getBean(HikariDataSource.class);
					assertThat(ds.getDataSourceProperties()
							.getProperty("dataSourceClassName"))
									.isEqualTo("org.h2.JDBCDataSource");

				});
	}

	@Test
	public void testDataSourceDefaultsPreserved() {
		this.contextRunner.run((context) -> {
			HikariDataSource ds = context.getBean(HikariDataSource.class);
			assertThat(ds.getMaxLifetime()).isEqualTo(1800000);
		});
	}

	@Test
	public void nameIsAliasedToPoolName() {
		this.contextRunner.withPropertyValues("spring.datasource.name=myDS")
				.run((context) -> {
					HikariDataSource ds = context.getBean(HikariDataSource.class);
					assertThat(ds.getPoolName()).isEqualTo("myDS");

				});
	}

	@Test
	public void poolNameTakesPrecedenceOverName() {
		this.contextRunner
				.withPropertyValues("spring.datasource.name=myDS",
						"spring.datasource.hikari.pool-name=myHikariDS")
				.run((context) -> {
					HikariDataSource ds = context.getBean(HikariDataSource.class);
					assertThat(ds.getPoolName()).isEqualTo("myHikariDS");
				});
	}

	@SuppressWarnings("unchecked")
	public static <T> T getField(Class<?> target, String name) {
		Field field = ReflectionUtils.findField(target, name, null);
		ReflectionUtils.makeAccessible(field);
		return (T) ReflectionUtils.getField(field, target);
	}

}
