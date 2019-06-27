

package org.springframework.boot.actuate.metrics.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

/**
 * Default implementation of {@link WebMvcTagsProvider}.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
public class DefaultWebMvcTagsProvider implements WebMvcTagsProvider {

	@Override
	public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response,
			Object handler, Throwable exception) {
		return Tags.of(WebMvcTags.method(request), WebMvcTags.uri(request, response),
				WebMvcTags.exception(exception), WebMvcTags.status(response));
	}

	@Override
	public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
		return Tags.of(WebMvcTags.method(request), WebMvcTags.uri(request, null));
	}

}
