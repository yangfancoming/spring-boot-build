

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link JdbcTest}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class JdbcTestWithAutoConfigureTestDatabaseReplaceAutoConfiguredWithoutOverrideIntegrationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	public void usesDefaultEmbeddedDatabase() throws Exception {
		String product = this.dataSource.getConnection().getMetaData()
				.getDatabaseProductName();
		// @AutoConfigureTestDatabase would use H2 but HSQL is manually defined
		assertThat(product).startsWith("HSQL");
	}

}
