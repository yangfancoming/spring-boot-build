

package org.springframework.boot.test.autoconfigure.web.client;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link RestClientTest} with a single client.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@RestClientTest(ExampleRestClient.class)
public class RestClientTestWithComponentIntegrationTests {

	@Autowired
	private MockRestServiceServer server;

	@Autowired
	private ExampleRestClient client;

	@Test
	public void mockServerCall() {
		this.server.expect(requestTo("/test"))
				.andRespond(withSuccess("hello", MediaType.TEXT_HTML));
		assertThat(this.client.test()).isEqualTo("hello");
	}

}
