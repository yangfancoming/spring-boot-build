

package org.springframework.boot.devtools.tunnel.server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link StaticPortProvider}.
 *
 * @author Phillip Webb
 */
public class StaticPortProviderTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void portMustBePositive() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Port must be positive");
		new StaticPortProvider(0);
	}

	@Test
	public void getPort() {
		StaticPortProvider provider = new StaticPortProvider(123);
		assertThat(provider.getPort()).isEqualTo(123);
	}

}
