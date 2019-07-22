

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link JdbcTest}.
 */
@RunWith(SpringRunner.class)
@JdbcTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQL)
@TestPropertySource(properties = "spring.test.database.replace=AUTO_CONFIGURED")
public class JdbcTestWithAutoConfigureTestDatabaseReplacePropertyAutoConfiguredIntegrationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	public void replacesAutoConfiguredDataSource() throws Exception {
		String product = this.dataSource.getConnection().getMetaData().getDatabaseProductName();
		assertThat(product).startsWith("HSQL");
	}

	@Configuration
	@EnableAutoConfiguration // Will auto-configure H2
	static class Config {

	}

}
