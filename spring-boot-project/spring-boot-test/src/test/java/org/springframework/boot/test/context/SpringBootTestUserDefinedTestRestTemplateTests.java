

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} configured with a user-defined {@link RestTemplate}
 * that is named {@code testRestTemplate}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "value=123" })
public class SpringBootTestUserDefinedTestRestTemplateTests
		extends AbstractSpringBootTestWebServerWebEnvironmentTests {

	@Test
	public void restTemplateIsUserDefined() {
		assertThat(getContext().getBean("testRestTemplate"))
				.isInstanceOf(RestTemplate.class);
	}

	// gh-7711

	@Configuration
	@EnableWebMvc
	@RestController
	protected static class Config extends AbstractConfig {

		@Bean
		public RestTemplate testRestTemplate() {
			return new RestTemplate();
		}

	}

}
