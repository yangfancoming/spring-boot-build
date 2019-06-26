

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;

/**
 * Hamcrest matcher to check the {@link AuthorizationExceptionMatcher} {@link Reason}.
 *
 * @author Madhura Bhave
 */
public final class AuthorizationExceptionMatcher {

	private AuthorizationExceptionMatcher() {
	}

	public static Matcher<?> withReason(Reason reason) {
		return new CustomMatcher<Object>(
				"CloudFoundryAuthorizationException with " + reason + " reason") {

			@Override
			public boolean matches(Object object) {
				return ((object instanceof CloudFoundryAuthorizationException)
						&& ((CloudFoundryAuthorizationException) object)
								.getReason() == reason);
			}

		};
	}

}
