

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AccessLevel}.
 *
 * @author Madhura Bhave
 */
public class AccessLevelTests {

	@Test
	public void accessToHealthEndpointShouldNotBeRestricted() {
		assertThat(AccessLevel.RESTRICTED.isAccessAllowed("health")).isTrue();
		assertThat(AccessLevel.FULL.isAccessAllowed("health")).isTrue();
	}

	@Test
	public void accessToInfoEndpointShouldNotBeRestricted() {
		assertThat(AccessLevel.RESTRICTED.isAccessAllowed("info")).isTrue();
		assertThat(AccessLevel.FULL.isAccessAllowed("info")).isTrue();
	}

	@Test
	public void accessToDiscoveryEndpointShouldNotBeRestricted() {
		assertThat(AccessLevel.RESTRICTED.isAccessAllowed("")).isTrue();
		assertThat(AccessLevel.FULL.isAccessAllowed("")).isTrue();
	}

	@Test
	public void accessToAnyOtherEndpointShouldBeRestricted() {
		assertThat(AccessLevel.RESTRICTED.isAccessAllowed("env")).isFalse();
		assertThat(AccessLevel.FULL.isAccessAllowed("")).isTrue();
	}

}
