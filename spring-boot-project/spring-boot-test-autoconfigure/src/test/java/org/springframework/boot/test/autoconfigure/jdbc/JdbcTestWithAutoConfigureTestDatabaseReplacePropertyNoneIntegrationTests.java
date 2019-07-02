

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource(properties = "spring.test.database.replace=NONE")
public class JdbcTestWithAutoConfigureTestDatabaseReplacePropertyNoneIntegrationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	public void usesDefaultEmbeddedDatabase() throws Exception {
		// HSQL is explicitly defined and should not be replaced
		String product = this.dataSource.getConnection().getMetaData()
				.getDatabaseProductName();
		assertThat(product).startsWith("HSQL");
	}

}
