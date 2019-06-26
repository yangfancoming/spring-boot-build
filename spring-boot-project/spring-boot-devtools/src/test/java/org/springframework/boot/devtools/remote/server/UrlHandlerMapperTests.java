

package org.springframework.boot.devtools.remote.server;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link UrlHandlerMapper}.
 *
 * @author Rob Winch
 * @author Phillip Webb
 */
public class UrlHandlerMapperTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Handler handler = mock(Handler.class);

	@Test
	public void requestUriMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("URL must not be empty");
		new UrlHandlerMapper(null, this.handler);
	}

	@Test
	public void requestUriMustNotBeEmpty() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("URL must not be empty");
		new UrlHandlerMapper("", this.handler);
	}

	@Test
	public void requestUrlMustStartWithSlash() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("URL must start with '/'");
		new UrlHandlerMapper("tunnel", this.handler);
	}

	@Test
	public void handlesMatchedUrl() {
		UrlHandlerMapper mapper = new UrlHandlerMapper("/tunnel", this.handler);
		HttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/tunnel");
		ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		assertThat(mapper.getHandler(request)).isEqualTo(this.handler);
	}

	@Test
	public void ignoresDifferentUrl() {
		UrlHandlerMapper mapper = new UrlHandlerMapper("/tunnel", this.handler);
		HttpServletRequest servletRequest = new MockHttpServletRequest("GET",
				"/tunnel/other");
		ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		assertThat(mapper.getHandler(request)).isNull();
	}

}
