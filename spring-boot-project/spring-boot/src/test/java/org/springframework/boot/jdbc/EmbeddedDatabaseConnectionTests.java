

package org.springframework.boot.jdbc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EmbeddedDatabaseConnection}.
 *
 * @author Stephane Nicoll
 */
public class EmbeddedDatabaseConnectionTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void h2CustomDatabaseName() {
		assertThat(EmbeddedDatabaseConnection.H2.getUrl("mydb"))
				.isEqualTo("jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
	}

	@Test
	public void derbyCustomDatabaseName() {
		assertThat(EmbeddedDatabaseConnection.DERBY.getUrl("myderbydb"))
				.isEqualTo("jdbc:derby:memory:myderbydb;create=true");
	}

	@Test
	public void hsqlCustomDatabaseName() {
		assertThat(EmbeddedDatabaseConnection.HSQL.getUrl("myhsql"))
				.isEqualTo("jdbc:hsqldb:mem:myhsql");
	}

	@Test
	public void getUrlWithNullDatabaseName() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("DatabaseName must not be empty");
		EmbeddedDatabaseConnection.HSQL.getUrl(null);
	}

	@Test
	public void getUrlWithEmptyDatabaseName() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("DatabaseName must not be empty");
		EmbeddedDatabaseConnection.HSQL.getUrl("  ");
	}

}
