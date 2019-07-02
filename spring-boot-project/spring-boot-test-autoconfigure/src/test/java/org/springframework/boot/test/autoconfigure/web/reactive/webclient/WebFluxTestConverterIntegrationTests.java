

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Tests for {@link WebFluxTest} to validate converters are discovered.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ExampleController2.class)
public class WebFluxTestConverterIntegrationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void shouldFindConverter() {
		UUID id = UUID.randomUUID();
		this.webClient.get().uri("/two/" + id).exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo(id + "two");
	}

}
