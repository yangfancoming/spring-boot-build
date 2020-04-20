

package org.springframework.boot.actuate.autoconfigure.health;

import java.security.Principal;

import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.HealthWebEndpointResponseMapper;
import org.springframework.boot.actuate.health.ReactiveHealthEndpointWebExtension;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link HealthEndpointAutoConfiguration} in a reactive environment.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
public class ReactiveHealthEndpointWebExtensionTests {

	private ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withUserConfiguration(HealthIndicatorAutoConfiguration.class,
					HealthEndpointAutoConfiguration.class);

	@Test
	public void runShouldCreateExtensionBeans() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(ReactiveHealthEndpointWebExtension.class));
	}

	@Test
	public void runWhenHealthEndpointIsDisabledShouldNotCreateExtensionBeans() {
		this.contextRunner.withPropertyValues("management.endpoint.health.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ReactiveHealthEndpointWebExtension.class));
	}

	@Test
	public void runWithCustomHealthMappingShouldMapStatusCode() {
		this.contextRunner
				.withPropertyValues("management.health.status.http-mapping.CUSTOM=500")
				.run((context) -> {
					Object extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					HealthWebEndpointResponseMapper responseMapper = (HealthWebEndpointResponseMapper) ReflectionTestUtils
							.getField(extension, "responseMapper");
					Class<SecurityContext> securityContext = SecurityContext.class;
					assertThat(responseMapper
							.map(Health.down().build(), mock(securityContext))
							.getStatus()).isEqualTo(503);
					assertThat(responseMapper.map(Health.status("OUT_OF_SERVICE").build(),
							mock(securityContext)).getStatus()).isEqualTo(503);
					assertThat(responseMapper
							.map(Health.status("CUSTOM").build(), mock(securityContext))
							.getStatus()).isEqualTo(500);
				});
	}

	@Test
	public void regularAndReactiveHealthIndicatorsMatch() {
		this.contextRunner
				.withPropertyValues("management.endpoint.health.show-details=always")
				.withUserConfiguration(HealthIndicatorsConfiguration.class)
				.run((context) -> {
					HealthEndpoint endpoint = context.getBean(HealthEndpoint.class);
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					Health endpointHealth = endpoint.health();
					SecurityContext securityContext = mock(SecurityContext.class);
					given(securityContext.getPrincipal())
							.willReturn(mock(Principal.class));
					Health extensionHealth = extension.health(securityContext).block()
							.getBody();
					assertThat(endpointHealth.getDetails())
							.containsOnlyKeys("application", "first", "second");
					assertThat(extensionHealth.getDetails())
							.containsOnlyKeys("application", "first", "second");
				});
	}

	@Test
	public void unauthenticatedUsersAreNotShownDetailsByDefault() {
		this.contextRunner.run((context) -> {
			ReactiveHealthEndpointWebExtension extension = context
					.getBean(ReactiveHealthEndpointWebExtension.class);
			assertThat(extension.health(mock(SecurityContext.class)).block().getBody()
					.getDetails()).isEmpty();
		});
	}

	@Test
	public void authenticatedUsersAreNotShownDetailsByDefault() {
		this.contextRunner.run((context) -> {
			ReactiveHealthEndpointWebExtension extension = context
					.getBean(ReactiveHealthEndpointWebExtension.class);
			SecurityContext securityContext = mock(SecurityContext.class);
			given(securityContext.getPrincipal()).willReturn(mock(Principal.class));
			assertThat(extension.health(securityContext).block().getBody().getDetails())
					.isEmpty();
		});
	}

	@Test
	public void authenticatedUsersWhenAuthorizedCanBeShownDetails() {
		this.contextRunner
				.withPropertyValues(
						"management.endpoint.health.show-details=when-authorized")
				.run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					SecurityContext securityContext = mock(SecurityContext.class);
					given(securityContext.getPrincipal())
							.willReturn(mock(Principal.class));
					assertThat(extension.health(securityContext).block().getBody()
							.getDetails()).isNotEmpty();
				});
	}

	@Test
	public void unauthenticatedUsersCanBeShownDetails() {
		this.contextRunner
				.withPropertyValues("management.endpoint.health.show-details=always")
				.run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					assertThat(extension.health(null).block().getBody().getDetails())
							.isNotEmpty();
				});
	}

	@Test
	public void detailsCanBeHiddenFromAuthenticatedUsers() {
		this.contextRunner
				.withPropertyValues("management.endpoint.health.show-details=never")
				.run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					SecurityContext securityContext = mock(SecurityContext.class);
					assertThat(extension.health(securityContext).block().getBody()
							.getDetails()).isEmpty();
				});
	}

	@Test
	public void detailsCanBeHiddenFromUnauthorizedUsers() {
		this.contextRunner.withPropertyValues(
				"management.endpoint.health.show-details=when-authorized",
				"management.endpoint.health.roles=ACTUATOR").run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					SecurityContext securityContext = mock(SecurityContext.class);
					given(securityContext.getPrincipal())
							.willReturn(mock(Principal.class));
					given(securityContext.isUserInRole("ACTUATOR")).willReturn(false);
					assertThat(extension.health(securityContext).block().getBody()
							.getDetails()).isEmpty();
				});
	}

	@Test
	public void detailsCanBeShownToAuthorizedUsers() {
		this.contextRunner.withPropertyValues(
				"management.endpoint.health.show-details=when-authorized",
				"management.endpoint.health.roles=ACTUATOR").run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					SecurityContext securityContext = mock(SecurityContext.class);
					given(securityContext.getPrincipal())
							.willReturn(mock(Principal.class));
					given(securityContext.isUserInRole("ACTUATOR")).willReturn(true);
					assertThat(extension.health(securityContext).block().getBody()
							.getDetails()).isNotEmpty();
				});
	}

	@Test
	public void roleCanBeCustomized() {
		this.contextRunner.withPropertyValues(
				"management.endpoint.health.show-details=when-authorized",
				"management.endpoint.health.roles=ADMIN").run((context) -> {
					ReactiveHealthEndpointWebExtension extension = context
							.getBean(ReactiveHealthEndpointWebExtension.class);
					SecurityContext securityContext = mock(SecurityContext.class);
					given(securityContext.getPrincipal())
							.willReturn(mock(Principal.class));
					given(securityContext.isUserInRole("ADMIN")).willReturn(true);
					assertThat(extension.health(securityContext).block().getBody()
							.getDetails()).isNotEmpty();
				});
	}

	@Configuration
	static class HealthIndicatorsConfiguration {

		@Bean
		public HealthIndicator firstHealthIndicator() {
			return () -> Health.up().build();
		}

		@Bean
		public ReactiveHealthIndicator secondHealthIndicator() {
			return () -> Mono.just(Health.up().build());
		}

	}

}