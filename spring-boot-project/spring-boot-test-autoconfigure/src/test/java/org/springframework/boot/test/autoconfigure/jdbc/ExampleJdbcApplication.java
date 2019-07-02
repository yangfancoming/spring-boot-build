

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Example {@link SpringBootApplication} used with {@link JdbcTest} tests.
 *
 * @author Phillip Webb
 */
@SpringBootApplication
public class ExampleJdbcApplication {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
				.generateUniqueName(true).setType(EmbeddedDatabaseType.HSQL);
		return builder.build();
	}

}
