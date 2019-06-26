

package org.springframework.boot.autoconfigure.jdbc.metadata;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.metadata.CommonsDbcp2DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.TomcatDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Register the {@link DataSourcePoolMetadataProvider} instances for the supported data
 * sources.
 *
 * @author Stephane Nicoll
 * @since 1.2.0
 */
@Configuration
public class DataSourcePoolMetadataProvidersConfiguration {

	@Configuration
	@ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
	static class TomcatDataSourcePoolMetadataProviderConfiguration {

		@Bean
		public DataSourcePoolMetadataProvider tomcatPoolDataSourceMetadataProvider() {
			return (dataSource) -> {
				if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
					return new TomcatDataSourcePoolMetadata(
							(org.apache.tomcat.jdbc.pool.DataSource) dataSource);
				}
				return null;
			};
		}

	}

	@Configuration
	@ConditionalOnClass(HikariDataSource.class)
	static class HikariPoolDataSourceMetadataProviderConfiguration {

		@Bean
		public DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider() {
			return (dataSource) -> {
				if (dataSource instanceof HikariDataSource) {
					return new HikariDataSourcePoolMetadata(
							(HikariDataSource) dataSource);
				}
				return null;
			};
		}

	}

	@Configuration
	@ConditionalOnClass(BasicDataSource.class)
	static class CommonsDbcp2PoolDataSourceMetadataProviderConfiguration {

		@Bean
		public DataSourcePoolMetadataProvider commonsDbcp2PoolDataSourceMetadataProvider() {
			return (dataSource) -> {
				if (dataSource instanceof BasicDataSource) {
					return new CommonsDbcp2DataSourcePoolMetadata(
							(BasicDataSource) dataSource);
				}
				return null;
			};
		}

	}

}
