

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} with {@link AutoConfigureWebTestClient} (i.e. full
 * integration test).
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive", classes = {
		WebTestClientSpringBootTestIntegrationTests.TestConfiguration.class,
		ExampleWebFluxApplication.class })
@AutoConfigureWebTestClient
public class WebTestClientSpringBootTestIntegrationTests {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private ApplicationContext applicationContext;

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

	@Test
	public void shouldHaveRealService() {
		assertThat(this.applicationContext.getBeansOfType(ExampleRealService.class))
				.hasSize(1);
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
			return http.authorizeExchange().anyExchange().permitAll().and().build();
		}

	}

}
