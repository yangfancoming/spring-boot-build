

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link AutoConfigureTestDatabase} when there are multiple
 * datasources.
 *
 * @author Greg Potter
 */
@RunWith(SpringRunner.class)
@JdbcTest
@AutoConfigureTestDatabase
public class AutoConfigureTestDatabaseWithMultipleDatasourcesIntegrationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	public void replacesDefinedDataSourceWithExplicit() throws Exception {
		// Look that the datasource is replaced with an H2 DB.
		String product = this.dataSource.getConnection().getMetaData()
				.getDatabaseProductName();
		assertThat(product).startsWith("H2");
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		@Primary
		public DataSource dataSource() {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
					.generateUniqueName(true).setType(EmbeddedDatabaseType.HSQL);
			return builder.build();
		}

		@Bean
		public DataSource secondaryDataSource() {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
					.generateUniqueName(true).setType(EmbeddedDatabaseType.HSQL);
			return builder.build();
		}

	}

}
