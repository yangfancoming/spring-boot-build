

package org.springframework.boot.autoconfigure.security.reactive;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PathRequest}.
 *
 * @author Madhura Bhave
 */
public class PathRequestTests {

	@Test
	public void toStaticResourcesShouldReturnStaticResourceRequest() {
		assertThat(PathRequest.toStaticResources())
				.isInstanceOf(StaticResourceRequest.class);
	}

}
