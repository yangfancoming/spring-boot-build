

package org.springframework.boot.actuate.endpoint.web;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebEndpointResponse}.
 *
 * @author Phillip Webb
 */
public class WebEndpointResponseTests {

	@Test
	public void createWithNoParamsShouldReturn200() {
		WebEndpointResponse<Object> response = new WebEndpointResponse<>();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isNull();
	}

	@Test
	public void createWithStatusShouldReturnStatus() {
		WebEndpointResponse<Object> response = new WebEndpointResponse<>(404);
		assertThat(response.getStatus()).isEqualTo(404);
		assertThat(response.getBody()).isNull();
	}

	@Test
	public void createWithBodyShouldReturnBody() {
		WebEndpointResponse<Object> response = new WebEndpointResponse<>("body");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isEqualTo("body");
	}

	@Test
	public void createWithBodyAndStatusShouldReturnStatusAndBody() {
		WebEndpointResponse<Object> response = new WebEndpointResponse<>("body", 500);
		assertThat(response.getStatus()).isEqualTo(500);
		assertThat(response.getBody()).isEqualTo("body");
	}

}
