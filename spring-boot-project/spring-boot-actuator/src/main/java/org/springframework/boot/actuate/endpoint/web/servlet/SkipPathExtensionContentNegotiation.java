

package org.springframework.boot.actuate.endpoint.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * {@link HandlerInterceptorAdapter} to ensure that
 * {@link PathExtensionContentNegotiationStrategy} is skipped for web endpoints.
 *
 * @author Phillip Webb
 */
final class SkipPathExtensionContentNegotiation extends HandlerInterceptorAdapter {

	private static final String SKIP_ATTRIBUTE = PathExtensionContentNegotiationStrategy.class
			.getName() + ".SKIP";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		request.setAttribute(SKIP_ATTRIBUTE, Boolean.TRUE);
		return true;
	}

}
