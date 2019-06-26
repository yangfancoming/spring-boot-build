

package org.springframework.boot.test.context;

import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Tests for {@link SpringBootTest} configured with {@link WebEnvironment#DEFINED_PORT}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, properties = {
		"server.port=0", "value=123" })
public class SpringBootTestWebEnvironmentDefinedPortTests
		extends AbstractSpringBootTestWebServerWebEnvironmentTests {

	@Configuration
	@EnableWebMvc
	@RestController
	protected static class Config extends AbstractConfig {

	}

}
