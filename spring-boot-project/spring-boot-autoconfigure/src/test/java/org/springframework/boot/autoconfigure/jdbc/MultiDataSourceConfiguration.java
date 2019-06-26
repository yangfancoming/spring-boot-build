

package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for multiple {@link DataSource}.
 *
 * @author Phillip Webb
 * @author Kazuki Shimizu
 */
@Configuration
class MultiDataSourceConfiguration {

	@Bean
	public DataSource test1DataSource() {
		return new TestDataSource("test1");
	}

	@Bean
	public DataSource test2DataSource() {
		return new TestDataSource("test2");
	}

}
