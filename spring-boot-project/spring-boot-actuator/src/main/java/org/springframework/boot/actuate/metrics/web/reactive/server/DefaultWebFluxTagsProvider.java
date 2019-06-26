

package org.springframework.boot.actuate.metrics.web.reactive.server;

import java.util.Arrays;

import io.micrometer.core.instrument.Tag;

import org.springframework.web.server.ServerWebExchange;

/**
 * Default implementation of {@link WebFluxTagsProvider}.
 *
 * @author Jon Schneider
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class DefaultWebFluxTagsProvider implements WebFluxTagsProvider {

	@Override
	public Iterable<Tag> httpRequestTags(ServerWebExchange exchange,
			Throwable exception) {
		return Arrays.asList(WebFluxTags.method(exchange), WebFluxTags.uri(exchange),
				WebFluxTags.exception(exception), WebFluxTags.status(exchange));
	}

}
