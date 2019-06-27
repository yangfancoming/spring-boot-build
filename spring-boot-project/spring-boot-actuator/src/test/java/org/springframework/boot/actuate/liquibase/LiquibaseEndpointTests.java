

package org.springframework.boot.actuate.liquibase;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LiquibaseEndpoint}.
 *
 * @author Eddú Meléndez
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class LiquibaseEndpointTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class,
					LiquibaseAutoConfiguration.class))
			.withPropertyValues("spring.datasource.generate-unique-name=true");

	@Test
	public void liquibaseReportIsReturned() {
		this.contextRunner.withUserConfiguration(Config.class)
				.run((context) -> assertThat(
						context.getBean(LiquibaseEndpoint.class).liquibaseBeans()
								.getContexts().get(context.getId()).getLiquibaseBeans())
										.hasSize(1));
	}

	@Test
	public void invokeWithCustomSchema() {
		this.contextRunner.withUserConfiguration(Config.class)
				.withPropertyValues("spring.liquibase.default-schema=CUSTOMSCHEMA",
						"spring.datasource.schema=classpath:/db/create-custom-schema.sql")
				.run((context) -> assertThat(
						context.getBean(LiquibaseEndpoint.class).liquibaseBeans()
								.getContexts().get(context.getId()).getLiquibaseBeans())
										.hasSize(1));
	}

	@Test
	public void connectionAutoCommitPropertyIsReset() {
		this.contextRunner.withUserConfiguration(Config.class).run((context) -> {
			DataSource dataSource = context.getBean(DataSource.class);
			assertThat(getAutoCommit(dataSource)).isTrue();
			context.getBean(LiquibaseEndpoint.class).liquibaseBeans();
			assertThat(getAutoCommit(dataSource)).isTrue();
		});
	}

	private boolean getAutoCommit(DataSource dataSource) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			System.out.println(connection);
			return connection.getAutoCommit();
		}
	}

	@Configuration
	public static class Config {

		@Bean
		public LiquibaseEndpoint endpoint(ApplicationContext context) {
			return new LiquibaseEndpoint(context);
		}

	}

}
