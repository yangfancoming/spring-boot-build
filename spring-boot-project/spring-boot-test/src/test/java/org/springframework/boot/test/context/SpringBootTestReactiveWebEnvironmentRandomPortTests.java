

package org.springframework.boot.test.context;

import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Tests for {@link SpringBootTest} in a reactive environment configured with
 * {@link WebEnvironment#RANDOM_PORT}.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
		"spring.main.webApplicationType=reactive", "value=123" })
public class SpringBootTestReactiveWebEnvironmentRandomPortTests
		extends AbstractSpringBootTestEmbeddedReactiveWebEnvironmentTests {

	@Configuration
	@EnableWebFlux
	@RestController
	protected static class Config extends AbstractConfig {

	}

}
