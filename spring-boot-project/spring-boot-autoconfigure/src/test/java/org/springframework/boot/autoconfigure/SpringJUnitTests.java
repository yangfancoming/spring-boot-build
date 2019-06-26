

package org.springframework.boot.autoconfigure;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class SpringJUnitTests {

	@Autowired
	private ApplicationContext context;

	@Value("${foo:spam}")
	private String foo = "bar";

	@Test
	public void testContextCreated() {
		assertThat(this.context).isNotNull();
	}

	@Test
	public void testContextInitialized() {
		assertThat(this.foo).isEqualTo("bucket");
	}

	@Configuration
	@Import({ PropertyPlaceholderAutoConfiguration.class })
	public static class TestConfiguration {

	}

}
