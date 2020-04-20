

package org.springframework.boot.actuate.autoconfigure.mail;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.mail.MailHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MailHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class MailHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MailSenderAutoConfiguration.class,
					MailHealthIndicatorAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class))
			.withPropertyValues("spring.mail.host:smtp.example.com");

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(MailHealthIndicator.class)
						.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.mail.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(MailHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
