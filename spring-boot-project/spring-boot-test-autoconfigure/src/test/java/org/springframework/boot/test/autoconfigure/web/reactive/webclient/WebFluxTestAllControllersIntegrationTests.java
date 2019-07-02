

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Tests for {@link WebFluxTest} when no explicit controller is defined.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebFluxTest
public class WebFluxTestAllControllersIntegrationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void shouldFindController1() {
		this.webClient.get().uri("/one").exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("one");
	}

	@Test
	public void shouldFindController2() {
		this.webClient.get().uri("/two").exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("two");
	}

}
