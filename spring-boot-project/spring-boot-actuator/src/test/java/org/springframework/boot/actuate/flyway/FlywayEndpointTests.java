

package org.springframework.boot.actuate.flyway;

import org.junit.Test;

import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayEndpoint}.
 *
 * @author Eddú Meléndez
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
public class FlywayEndpointTests {

	@Test
	public void flywayReportIsProduced() {
		new ApplicationContextRunner().withUserConfiguration(Config.class)
				.run((context) -> assertThat(
						context.getBean(FlywayEndpoint.class).flywayBeans().getContexts()
								.get(context.getId()).getFlywayBeans()).hasSize(1));
	}

	@Configuration
	@Import({ EmbeddedDataSourceConfiguration.class, FlywayAutoConfiguration.class })
	public static class Config {

		@Bean
		public FlywayEndpoint endpoint(ApplicationContext context) {
			return new FlywayEndpoint(context);
		}

	}

}
