

package org.springframework.boot.actuate.endpoint;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Sanitizer}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 */
public class SanitizerTests {

	@Test
	public void defaults() {
		Sanitizer sanitizer = new Sanitizer();
		assertThat(sanitizer.sanitize("password", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("my-password", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("my-OTHER.paSSword", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("somesecret", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("somekey", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("token", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("sometoken", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("find", "secret")).isEqualTo("secret");
		assertThat(sanitizer.sanitize("sun.java.command",
				"--spring.redis.password=pa55w0rd")).isEqualTo("******");
	}

	@Test
	public void regex() {
		Sanitizer sanitizer = new Sanitizer(".*lock.*");
		assertThat(sanitizer.sanitize("verylOCkish", "secret")).isEqualTo("******");
		assertThat(sanitizer.sanitize("veryokish", "secret")).isEqualTo("secret");
	}

}
