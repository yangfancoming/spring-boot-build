

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link SpringBootTest} with a custom inline server.port in a non-embedded web
 * environment.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "server.port=12345")
public class SpringBootTestCustomPortTests {

	@Autowired
	private Environment environment;

	@Test
	public void validatePortIsNotOverwritten() {
		String port = this.environment.getProperty("server.port");
		assertThat(port).isEqualTo("12345");
	}

	@Configuration
	protected static class Config {

	}

}
