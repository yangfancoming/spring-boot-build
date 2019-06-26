

package org.springframework.boot.actuate.autoconfigure.session;

import org.junit.Test;

import org.springframework.boot.actuate.session.SessionsEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FindByIndexNameSessionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SessionsEndpointAutoConfiguration}.
 *
 * @author Vedran Pavic
 */
public class SessionsEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(SessionsEndpointAutoConfiguration.class))
			.withUserConfiguration(SessionConfiguration.class);

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(SessionsEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("management.endpoint.sessions.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(SessionsEndpoint.class));
	}

	@Configuration
	static class SessionConfiguration {

		@Bean
		public FindByIndexNameSessionRepository<?> sessionRepository() {
			return mock(FindByIndexNameSessionRepository.class);
		}

	}

}
