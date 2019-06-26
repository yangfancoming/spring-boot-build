

package org.springframework.boot.web.embedded.jetty;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

/**
 * Variation of Jetty's {@link ErrorHandler} that supports all {@link HttpMethod
 * HttpMethods} rather than just {@code GET}, {@code POST} and {@code HEAD}. Jetty
 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=446039">intentionally only
 * supports a limited set of HTTP methods</a> for error pages, however, Spring Boot
 * prefers Tomcat, Jetty and Undertow to all behave in the same way.
 *
 * @author Phillip Webb
 */
class JettyEmbeddedErrorHandler extends ErrorHandler {

	private final ErrorHandler delegate;

	JettyEmbeddedErrorHandler(ErrorHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String method = request.getMethod();
		if (!HttpMethod.GET.is(method) && !HttpMethod.POST.is(method)
				&& !HttpMethod.HEAD.is(method)) {
			request = new ErrorHttpServletRequest(request);
		}
		this.delegate.handle(target, baseRequest, request, response);
	}

	private static class ErrorHttpServletRequest extends HttpServletRequestWrapper {

		private boolean simulateGetMethod = true;

		ErrorHttpServletRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getMethod() {
			return (this.simulateGetMethod ? HttpMethod.GET.toString()
					: super.getMethod());
		}

		@Override
		public ServletContext getServletContext() {
			this.simulateGetMethod = false;
			return super.getServletContext();
		}

	}

}
