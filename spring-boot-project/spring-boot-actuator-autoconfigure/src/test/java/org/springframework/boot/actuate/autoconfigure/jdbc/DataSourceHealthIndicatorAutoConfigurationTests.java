

package org.springframework.boot.actuate.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DataSourceHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class DataSourceHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class,
					DataSourceHealthIndicatorAutoConfiguration.class))
			.withPropertyValues("spring.datasource.initialization-mode=never");

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run((context) -> {
			context.getBean(DataSourceHealthIndicator.class);
			assertThat(context).hasSingleBean(DataSourceHealthIndicator.class)
					.doesNotHaveBean(ApplicationHealthIndicator.class);
		});
	}

	@Test
	public void runWhenMultipleDataSourceBeansShouldCreateCompositeIndicator() {
		this.contextRunner.withUserConfiguration(EmbeddedDataSourceConfiguration.class,
				DataSourceConfig.class).run((context) -> {
					assertThat(context).hasSingleBean(HealthIndicator.class);
					HealthIndicator indicator = context
							.getBean(CompositeHealthIndicator.class);
					assertThat(indicator.health().getDetails())
							.containsOnlyKeys("dataSource", "testDataSource");
				});
	}

	@Test
	public void runShouldFilterRoutingDataSource() {
		this.contextRunner
				.withUserConfiguration(EmbeddedDataSourceConfiguration.class,
						RoutingDatasourceConfig.class)
				.run((context) -> assertThat(context)
						.hasSingleBean(DataSourceHealthIndicator.class)
						.doesNotHaveBean(CompositeHealthIndicator.class));
	}

	@Test
	public void runWithValidationQueryPropertyShouldUseCustomQuery() {
		this.contextRunner
				.withUserConfiguration(DataSourceConfig.class,
						DataSourcePoolMetadataProvidersConfiguration.class)
				.withPropertyValues(
						"spring.datasource.test.validation-query:SELECT from FOOBAR")
				.run((context) -> {
					assertThat(context).hasSingleBean(HealthIndicator.class);
					DataSourceHealthIndicator indicator = context
							.getBean(DataSourceHealthIndicator.class);
					assertThat(indicator.getQuery()).isEqualTo("SELECT from FOOBAR");
				});
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withUserConfiguration(EmbeddedDataSourceConfiguration.class)
				.withPropertyValues("management.health.db.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(DataSourceHealthIndicator.class)
						.doesNotHaveBean(CompositeHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

	@Configuration
	@EnableConfigurationProperties
	protected static class DataSourceConfig {

		@Bean
		@ConfigurationProperties(prefix = "spring.datasource.test")
		public DataSource testDataSource() {
			return DataSourceBuilder.create()
					.type(org.apache.tomcat.jdbc.pool.DataSource.class)
					.driverClassName("org.hsqldb.jdbc.JDBCDriver")
					.url("jdbc:hsqldb:mem:test").username("sa").build();
		}

	}

	@Configuration
	protected static class RoutingDatasourceConfig {

		@Bean
		AbstractRoutingDataSource routingDataSource() {
			return mock(AbstractRoutingDataSource.class);
		}

	}

}
