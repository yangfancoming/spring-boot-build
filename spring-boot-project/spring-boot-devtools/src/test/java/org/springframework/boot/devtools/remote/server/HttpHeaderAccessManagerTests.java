

package org.springframework.boot.devtools.remote.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HttpHeaderAccessManager}.
 *
 * @author Rob Winch
 * @author Phillip Webb
 */
public class HttpHeaderAccessManagerTests {

	private static final String HEADER = "X-AUTH_TOKEN";

	private static final String SECRET = "password";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private MockHttpServletRequest request;

	private ServerHttpRequest serverRequest;

	private HttpHeaderAccessManager manager;

	@Before
	public void setup() {
		this.request = new MockHttpServletRequest("GET", "/");
		this.serverRequest = new ServletServerHttpRequest(this.request);
		this.manager = new HttpHeaderAccessManager(HEADER, SECRET);
	}

	@Test
	public void headerNameMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("HeaderName must not be empty");
		new HttpHeaderAccessManager(null, SECRET);
	}

	@Test
	public void headerNameMustNotBeEmpty() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("HeaderName must not be empty");
		new HttpHeaderAccessManager("", SECRET);
	}

	@Test
	public void expectedSecretMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ExpectedSecret must not be empty");
		new HttpHeaderAccessManager(HEADER, null);
	}

	@Test
	public void expectedSecretMustNotBeEmpty() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ExpectedSecret must not be empty");
		new HttpHeaderAccessManager(HEADER, "");
	}

	@Test
	public void allowsMatching() {
		this.request.addHeader(HEADER, SECRET);
		assertThat(this.manager.isAllowed(this.serverRequest)).isTrue();
	}

	@Test
	public void disallowsWrongSecret() {
		this.request.addHeader(HEADER, "wrong");
		assertThat(this.manager.isAllowed(this.serverRequest)).isFalse();
	}

	@Test
	public void disallowsNoSecret() {
		assertThat(this.manager.isAllowed(this.serverRequest)).isFalse();
	}

	@Test
	public void disallowsWrongHeader() {
		this.request.addHeader("X-WRONG", SECRET);
		assertThat(this.manager.isAllowed(this.serverRequest)).isFalse();
	}

}
