

package org.springframework.boot.actuate.session;

import java.util.Collections;

import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.actuate.endpoint.web.test.WebEndpointRunners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Integration tests for {@link SessionsEndpoint} exposed by Jersey, Spring MVC, and
 * WebFlux.
 *
 * @author Vedran Pavic
 */
@RunWith(WebEndpointRunners.class)
public class SessionsEndpointWebIntegrationTests {

	private static final Session session = new MapSession();

	@SuppressWarnings("unchecked")
	private static final FindByIndexNameSessionRepository<Session> repository = mock(
			FindByIndexNameSessionRepository.class);

	private static WebTestClient client;

	@Test
	public void sessionsForUsernameWithoutUsernameParam() {
		client.get().uri((builder) -> builder.path("/actuator/sessions").build())
				.exchange().expectStatus().isBadRequest();
	}

	@Test
	public void sessionsForUsernameNoResults() {
		given(repository.findByIndexNameAndIndexValue(
				FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, "user"))
						.willReturn(Collections.emptyMap());
		client.get()
				.uri((builder) -> builder.path("/actuator/sessions")
						.queryParam("username", "user").build())
				.exchange().expectStatus().isOk().expectBody().jsonPath("sessions")
				.isEmpty();
	}

	@Test
	public void sessionsForUsernameFound() {
		given(repository.findByIndexNameAndIndexValue(
				FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, "user"))
						.willReturn(Collections.singletonMap(session.getId(), session));
		client.get()
				.uri((builder) -> builder.path("/actuator/sessions")
						.queryParam("username", "user").build())
				.exchange().expectStatus().isOk().expectBody().jsonPath("sessions.[*].id")
				.isEqualTo(new JSONArray().appendElement(session.getId()));
	}

	@Test
	public void sessionForIdNotFound() {
		client.get().uri((builder) -> builder
				.path("/actuator/sessions/session-id-not-found").build()).exchange()
				.expectStatus().isNotFound();
	}

	@Configuration
	protected static class TestConfiguration {

		@Bean
		public SessionsEndpoint sessionsEndpoint() {
			return new SessionsEndpoint(repository);
		}

	}

}
