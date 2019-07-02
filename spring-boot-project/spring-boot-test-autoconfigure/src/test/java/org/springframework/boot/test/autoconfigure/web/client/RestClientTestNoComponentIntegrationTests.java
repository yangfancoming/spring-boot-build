

package org.springframework.boot.test.autoconfigure.web.client;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link RestClientTest} with no specific client.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@RestClientTest
public class RestClientTestNoComponentIntegrationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Autowired
	private MockRestServiceServer server;

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void exampleRestClientIsNotInjected() {
		this.applicationContext.getBean(ExampleRestClient.class);
	}

	@Test
	public void manuallyCreateBean() {
		ExampleRestClient client = new ExampleRestClient(this.restTemplateBuilder);
		this.server.expect(requestTo("/test"))
				.andRespond(withSuccess("hello", MediaType.TEXT_HTML));
		assertThat(client.test()).isEqualTo("hello");
	}

}
