

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TestDatabaseAutoConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
public class TestDatabaseAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(TestDatabaseAutoConfiguration.class));

	@Test
	public void replaceWithNoDataSourceAvailable() {
		this.contextRunner
				.run((context) -> assertThat(context).doesNotHaveBean(DataSource.class));
	}

	@Test
	public void replaceWithUniqueDatabase() {
		this.contextRunner.withUserConfiguration(ExistingDataSourceConfiguration.class)
				.run((context) -> {
					DataSource datasource = context.getBean(DataSource.class);
					JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
					jdbcTemplate.execute("create table example (id int, name varchar);");
					this.contextRunner
							.withUserConfiguration(ExistingDataSourceConfiguration.class)
							.run((secondContext) -> {
								DataSource anotherDatasource = secondContext
										.getBean(DataSource.class);
								JdbcTemplate anotherJdbcTemplate = new JdbcTemplate(
										anotherDatasource);
								anotherJdbcTemplate.execute(
										"create table example (id int, name varchar);");
							});
				});
	}

	@Configuration
	static class ExistingDataSourceConfiguration {

		@Bean
		public DataSource dataSource() {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
					.generateUniqueName(true).setType(EmbeddedDatabaseType.HSQL);
			return builder.build();
		}

	}

}
