

package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link OAuth2ClientProperties}.
 *
 * @author Madhura Bhave
 */
public class OAuth2ClientPropertiesTests {

	private OAuth2ClientProperties properties = new OAuth2ClientProperties();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void clientIdAbsentThrowsException() {
		OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();
		registration.setClientSecret("secret");
		registration.setProvider("google");
		this.properties.getRegistration().put("foo", registration);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Client id must not be empty.");
		this.properties.validate();
	}

	@Test
	public void clientSecretAbsentThrowsException() {
		OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();
		registration.setClientId("foo");
		registration.setProvider("google");
		this.properties.getRegistration().put("foo", registration);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Client secret must not be empty.");
		this.properties.validate();
	}

}
