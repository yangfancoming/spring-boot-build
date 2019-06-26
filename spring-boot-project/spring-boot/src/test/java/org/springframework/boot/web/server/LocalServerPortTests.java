

package org.springframework.boot.web.server;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LocalServerPort}.
 *
 * @author Anand Shah
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@TestPropertySource(properties = "local.server.port=8181")
public class LocalServerPortTests {

	@Value("${local.server.port}")
	private String fromValue;

	@LocalServerPort
	private String fromAnnotation;

	@Test
	public void testLocalServerPortAnnotation() {
		assertThat(this.fromAnnotation).isNotNull().isEqualTo(this.fromValue);
	}

	@Configuration
	static class Config {

	}

}
