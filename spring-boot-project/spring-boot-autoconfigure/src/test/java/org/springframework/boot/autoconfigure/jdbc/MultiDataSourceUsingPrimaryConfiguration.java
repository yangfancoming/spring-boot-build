

package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for multiple {@link DataSource} (one being {@code @Primary}.
 *
 * @author Phillip Webb
 * @author Kazuki Shimizu
 */
@Configuration
class MultiDataSourceUsingPrimaryConfiguration {

	@Bean
	@Primary
	public DataSource test1DataSource() {
		return new TestDataSource("test1");
	}

	@Bean
	public DataSource test2DataSource() {
		return new TestDataSource("test2");
	}

}
