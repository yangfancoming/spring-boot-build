

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CloudFoundryAuthorizationException}.
 *
 * @author Madhura Bhave
 */
public class CloudFoundryAuthorizationExceptionTests {

	@Test
	public void statusCodeForInvalidTokenReasonShouldBe401() {
		assertThat(createException(Reason.INVALID_TOKEN).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForInvalidIssuerReasonShouldBe401() {
		assertThat(createException(Reason.INVALID_ISSUER).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForInvalidAudienceReasonShouldBe401() {
		assertThat(createException(Reason.INVALID_AUDIENCE).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForInvalidSignatureReasonShouldBe401() {
		assertThat(createException(Reason.INVALID_SIGNATURE).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForMissingAuthorizationReasonShouldBe401() {
		assertThat(createException(Reason.MISSING_AUTHORIZATION).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForUnsupportedSignatureAlgorithmReasonShouldBe401() {
		assertThat(createException(Reason.UNSUPPORTED_TOKEN_SIGNING_ALGORITHM)
				.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForTokenExpiredReasonShouldBe401() {
		assertThat(createException(Reason.TOKEN_EXPIRED).getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void statusCodeForAccessDeniedReasonShouldBe403() {
		assertThat(createException(Reason.ACCESS_DENIED).getStatusCode())
				.isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	public void statusCodeForServiceUnavailableReasonShouldBe503() {
		assertThat(createException(Reason.SERVICE_UNAVAILABLE).getStatusCode())
				.isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
	}

	private CloudFoundryAuthorizationException createException(Reason reason) {
		return new CloudFoundryAuthorizationException(reason, "message");
	}

}
