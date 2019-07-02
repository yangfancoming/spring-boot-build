

package org.springframework.boot.test.autoconfigure.jooq;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link JooqTest}.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@JooqTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class JooqTestWithAutoConfigureTestDatabaseIntegrationTests {

	@Autowired
	private DSLContext dsl;

	@Autowired
	private DataSource dataSource;

	@Test
	public void replacesAutoConfiguredDataSource() throws Exception {
		String product = this.dataSource.getConnection().getMetaData()
				.getDatabaseProductName();
		assertThat(product).startsWith("H2");
		assertThat(this.dsl.configuration().dialect()).isEqualTo(SQLDialect.H2);
	}

}
