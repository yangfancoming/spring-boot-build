

package org.springframework.boot.actuate.autoconfigure.ldap;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.ldap.LdapHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link LdapHealthIndicatorAutoConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
public class LdapHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(LdapConfiguration.class).withConfiguration(
					AutoConfigurations.of(LdapHealthIndicatorAutoConfiguration.class,
							HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(LdapHealthIndicator.class)
						.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.ldap.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(LdapHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

	@Configuration
	@AutoConfigureBefore(LdapHealthIndicatorAutoConfiguration.class)
	protected static class LdapConfiguration {

		@Bean
		public LdapOperations ldapOperations() {
			return mock(LdapOperations.class);
		}

	}

}
