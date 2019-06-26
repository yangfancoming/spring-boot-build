

package org.springframework.boot.autoconfigure.mustache;

import java.util.Collections;

import com.samskivert.mustache.Mustache;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for {@link MustacheAutoConfiguration} outside of a web application.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = { "env.FOO=There",
		"foo=World" })
public class MustacheStandaloneIntegrationTests {

	@Autowired
	private Mustache.Compiler compiler;

	@Test
	public void directCompilation() {
		assertThat(this.compiler.compile("Hello: {{world}}")
				.execute(Collections.singletonMap("world", "World")))
						.isEqualTo("Hello: World");
	}

	@Test
	public void environmentCollectorCompoundKey() {
		assertThat(this.compiler.compile("Hello: {{env.foo}}").execute(new Object()))
				.isEqualTo("Hello: There");
	}

	@Test
	public void environmentCollectorCompoundKeyStandard() {
		assertThat(this.compiler.standardsMode(true).compile("Hello: {{env.foo}}")
				.execute(new Object())).isEqualTo("Hello: There");
	}

	@Test
	public void environmentCollectorSimpleKey() {
		assertThat(this.compiler.compile("Hello: {{foo}}").execute(new Object()))
				.isEqualTo("Hello: World");
	}

	@Configuration
	@Import({ MustacheAutoConfiguration.class,
			PropertyPlaceholderAutoConfiguration.class })
	protected static class Application {

	}

}
