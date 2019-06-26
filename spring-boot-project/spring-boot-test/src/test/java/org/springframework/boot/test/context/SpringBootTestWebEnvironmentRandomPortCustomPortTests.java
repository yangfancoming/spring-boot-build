

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.AbstractSpringBootTestWebServerWebEnvironmentTests.AbstractConfig;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link SpringBootTest} with a custom inline server.port in an embedded web
 * environment.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
		"server.port=12345" })
public class SpringBootTestWebEnvironmentRandomPortCustomPortTests {

	@Autowired
	private Environment environment;

	@Test
	public void validatePortIsNotOverwritten() {
		String port = this.environment.getProperty("server.port");
		assertThat(port).isEqualTo("0");
	}

	@Configuration
	@EnableWebMvc
	protected static class Config extends AbstractConfig {

	}

}
