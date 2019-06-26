

package org.springframework.boot.autoconfigure.context;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MessageSourceAutoConfiguration}.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@SpringBootTest("spring.messages.basename:test/messages")
@ImportAutoConfiguration({ MessageSourceAutoConfiguration.class,
		PropertyPlaceholderAutoConfiguration.class })
@DirtiesContext
public class MessageSourceAutoConfigurationIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testMessageSourceFromPropertySourceAnnotation() {
		assertThat(this.context.getMessage("foo", null, "Foo message", Locale.UK))
				.isEqualTo("bar");
	}

	@Configuration
	protected static class Config {

	}

}
