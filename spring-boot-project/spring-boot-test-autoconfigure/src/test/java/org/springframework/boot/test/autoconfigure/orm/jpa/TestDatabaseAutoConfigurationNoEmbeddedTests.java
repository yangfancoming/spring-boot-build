

package org.springframework.boot.test.autoconfigure.orm.jpa;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Specific tests for {@link TestDatabaseAutoConfiguration} when no embedded database is
 * available.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions({ "h2-*.jar", "hsqldb-*.jar", "derby-*.jar" })
public class TestDatabaseAutoConfigurationNoEmbeddedTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(ExistingDataSourceConfiguration.class)
			.withConfiguration(
					AutoConfigurations.of(TestDatabaseAutoConfiguration.class));

	@Test
	public void applyAnyReplace() {
		this.contextRunner.run((context) -> assertThat(context).getFailure()
				.isInstanceOf(BeanCreationException.class)
				.hasMessageContaining(
						"Failed to replace DataSource with an embedded database for tests.")
				.hasMessageContaining(
						"If you want an embedded database please put a supported one on the classpath")
				.hasMessageContaining(
						"or tune the replace attribute of @AutoConfigureTestDatabase."));
	}

	@Test
	public void applyNoReplace() {
		this.contextRunner.withPropertyValues("spring.test.database.replace=NONE")
				.run((context) -> {
					assertThat(context).hasSingleBean(DataSource.class);
					assertThat(context).getBean(DataSource.class)
							.isSameAs(context.getBean("myCustomDataSource"));
				});
	}

	@Configuration
	static class ExistingDataSourceConfiguration {

		@Bean
		public DataSource myCustomDataSource() {
			return mock(DataSource.class);
		}

	}

}
