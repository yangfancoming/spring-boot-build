

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} configured with {@link WebEnvironment#RANDOM_PORT}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "value=123" })
public class SpringBootTestWebEnvironmentRandomPortTests
		extends AbstractSpringBootTestWebServerWebEnvironmentTests {

	@Test
	public void testRestTemplateShouldUseBuilder() {
		assertThat(getRestTemplate().getRestTemplate().getMessageConverters())
				.hasAtLeastOneElementOfType(MyConverter.class);
	}

	@Configuration
	@EnableWebMvc
	@RestController
	protected static class Config extends AbstractConfig {

		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder()
					.additionalMessageConverters(new MyConverter());

		}

	}

	private static class MyConverter extends StringHttpMessageConverter {

	}

}
