

package org.springframework.boot.web.embedded.undertow;

import java.net.InetAddress;

import javax.net.ssl.KeyManager;

import org.junit.Test;

import org.springframework.boot.web.server.Ssl;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SslBuilderCustomizer}
 *
 * @author Brian Clozel
 */
public class SslBuilderCustomizerTests {

	@Test
	public void getKeyManagersWhenAliasIsNullShouldNotDecorate() throws Exception {
		Ssl ssl = new Ssl();
		ssl.setKeyPassword("password");
		ssl.setKeyStore("src/test/resources/test.jks");
		SslBuilderCustomizer customizer = new SslBuilderCustomizer(8080,
				InetAddress.getLocalHost(), ssl, null);
		KeyManager[] keyManagers = ReflectionTestUtils.invokeMethod(customizer,
				"getKeyManagers", ssl, null);
		Class<?> name = Class.forName("org.springframework.boot.web.embedded.undertow"
				+ ".SslBuilderCustomizer$ConfigurableAliasKeyManager");
		assertThat(keyManagers[0]).isNotInstanceOf(name);
	}

}
