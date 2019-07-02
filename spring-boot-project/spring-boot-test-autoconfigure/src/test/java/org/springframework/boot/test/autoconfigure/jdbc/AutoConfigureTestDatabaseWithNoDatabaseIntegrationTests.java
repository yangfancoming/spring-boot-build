

package org.springframework.boot.test.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link AutoConfigureTestDatabase} when there is no database.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
public class AutoConfigureTestDatabaseWithNoDatabaseIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testContextLoads() {
		// gh-6897
		assertThat(this.context).isNotNull();
		assertThat(this.context.getBeanNamesForType(DataSource.class)).isNotEmpty();
	}

	@TestConfiguration
	static class Config {

	}

}
