

package org.springframework.boot.devtools.remote.client;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link HttpHeaderInterceptor}.
 *
 * @author Rob Winch
 * @since 1.3.0
 */
public class HttpHeaderInterceptorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String name;

	private String value;

	private HttpHeaderInterceptor interceptor;

	private HttpRequest request;

	private byte[] body;

	@Mock
	private ClientHttpRequestExecution execution;

	@Mock
	private ClientHttpResponse response;

	private MockHttpServletRequest httpRequest;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.body = new byte[] {};
		this.httpRequest = new MockHttpServletRequest();
		this.request = new ServletServerHttpRequest(this.httpRequest);
		this.name = "X-AUTH-TOKEN";
		this.value = "secret";
		given(this.execution.execute(this.request, this.body)).willReturn(this.response);
		this.interceptor = new HttpHeaderInterceptor(this.name, this.value);
	}

	@Test
	public void constructorNullHeaderName() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Name must not be empty");
		new HttpHeaderInterceptor(null, this.value);
	}

	@Test
	public void constructorEmptyHeaderName() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Name must not be empty");
		new HttpHeaderInterceptor("", this.value);
	}

	@Test
	public void constructorNullHeaderValue() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Value must not be empty");
		new HttpHeaderInterceptor(this.name, null);
	}

	@Test
	public void constructorEmptyHeaderValue() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Value must not be empty");
		new HttpHeaderInterceptor(this.name, "");
	}

	@Test
	public void intercept() throws IOException {
		ClientHttpResponse result = this.interceptor.intercept(this.request, this.body,
				this.execution);
		assertThat(this.request.getHeaders().getFirst(this.name)).isEqualTo(this.value);
		assertThat(result).isEqualTo(this.response);
	}

}
