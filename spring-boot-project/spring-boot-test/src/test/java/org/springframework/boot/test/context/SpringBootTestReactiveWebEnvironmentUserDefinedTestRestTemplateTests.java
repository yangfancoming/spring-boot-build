

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} in a reactive environment configured with a
 * user-defined {@link RestTemplate} that is named {@code testRestTemplate}.
 *
 * @author Madhura Bhave
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
		"spring.main.web-application-type=reactive", "value=123" })
public class SpringBootTestReactiveWebEnvironmentUserDefinedTestRestTemplateTests
		extends AbstractSpringBootTestEmbeddedReactiveWebEnvironmentTests {

	@Test
	public void restTemplateIsUserDefined() {
		assertThat(getContext().getBean("testRestTemplate"))
				.isInstanceOf(RestTemplate.class);
	}

	@Configuration
	@EnableWebFlux
	@RestController
	protected static class Config extends AbstractConfig {

		@Bean
		public RestTemplate testRestTemplate() {
			return new RestTemplate();
		}

	}

}
