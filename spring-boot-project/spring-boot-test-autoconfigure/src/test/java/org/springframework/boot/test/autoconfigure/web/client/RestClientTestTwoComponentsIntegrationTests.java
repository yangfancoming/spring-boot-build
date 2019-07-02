

package org.springframework.boot.test.autoconfigure.web.client;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link RestClientTest} with two clients.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@RestClientTest({ ExampleRestClient.class, AnotherExampleRestClient.class })
public class RestClientTestTwoComponentsIntegrationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private ExampleRestClient client1;

	@Autowired
	private AnotherExampleRestClient client2;

	@Autowired
	private MockServerRestTemplateCustomizer customizer;

	@Autowired
	private MockRestServiceServer server;

	@Test
	public void serverShouldNotWork() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Unable to use auto-configured");
		this.server.expect(requestTo("/test"))
				.andRespond(withSuccess("hello", MediaType.TEXT_HTML));
	}

	@Test
	public void client1RestCallViaCustomizer() {
		this.customizer.getServer(this.client1.getRestTemplate())
				.expect(requestTo("/test"))
				.andRespond(withSuccess("hello", MediaType.TEXT_HTML));
		assertThat(this.client1.test()).isEqualTo("hello");
	}

	@Test
	public void client2RestCallViaCustomizer() {
		this.customizer.getServer(this.client2.getRestTemplate())
				.expect(requestTo("/test"))
				.andRespond(withSuccess("there", MediaType.TEXT_HTML));
		assertThat(this.client2.test()).isEqualTo("there");
	}

}
