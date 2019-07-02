

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WebFluxTest} and {@link MessageSource} auto-configuration.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebFluxTest
@TestPropertySource(properties = "spring.messages.basename=web-test-messages")
public class WebFluxTestMessageSourceIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void messageSourceHasBeenAutoConfigured() {
		assertThat(this.context.getMessage("a", null, Locale.ENGLISH)).isEqualTo("alpha");
	}

}
