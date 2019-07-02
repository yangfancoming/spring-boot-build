

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Tests for {@link WebFluxTest} when a specific controller is defined.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ExampleController1.class)
public class WebFluxTestOneControllerIntegrationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void shouldFindController() {
		this.webClient.get().uri("/one").exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("one");
	}

	@Test
	public void shouldNotScanOtherController() {
		this.webClient.get().uri("/two").exchange().expectStatus().isNotFound();
	}

}
