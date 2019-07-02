

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WebMvcTest} and {@link MessageSource} auto-configuration.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@TestPropertySource(properties = "spring.messages.basename=web-test-messages")
public class WebMvcTestMessageSourceIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void messageSourceHasBeenAutoConfigured() {
		assertThat(this.context.getMessage("a", null, Locale.ENGLISH)).isEqualTo("alpha");
	}

}
