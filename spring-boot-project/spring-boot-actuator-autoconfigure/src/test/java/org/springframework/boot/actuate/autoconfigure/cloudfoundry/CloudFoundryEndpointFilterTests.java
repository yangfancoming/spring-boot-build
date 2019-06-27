

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.junit.Test;

import org.springframework.boot.actuate.endpoint.annotation.DiscoveredEndpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CloudFoundryEndpointFilter}.
 *
 * @author Madhura Bhave
 */
public class CloudFoundryEndpointFilterTests {

	private CloudFoundryEndpointFilter filter = new CloudFoundryEndpointFilter();

	@Test
	public void matchIfDiscovererCloudFoundryShouldReturnFalse() {
		DiscoveredEndpoint<?> endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(CloudFoundryWebEndpointDiscoverer.class))
				.willReturn(true);
		assertThat(this.filter.match(endpoint)).isTrue();
	}

	@Test
	public void matchIfDiscovererNotCloudFoundryShouldReturnFalse() {
		DiscoveredEndpoint<?> endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(CloudFoundryWebEndpointDiscoverer.class))
				.willReturn(false);
		assertThat(this.filter.match(endpoint)).isFalse();
	}

}
