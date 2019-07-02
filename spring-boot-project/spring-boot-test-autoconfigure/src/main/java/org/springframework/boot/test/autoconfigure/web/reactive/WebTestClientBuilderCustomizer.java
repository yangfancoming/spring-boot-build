

package org.springframework.boot.test.autoconfigure.web.reactive;

import org.springframework.test.web.reactive.server.WebTestClient.Builder;

/**
 * A customizer for a {@link Builder}. Any {@code WebTestClientBuilderCustomizer} beans
 * found in the application context will be {@link #customize called} to customize the
 * auto-configured {@link Builder}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 * @see WebTestClientAutoConfiguration
 */
@FunctionalInterface
public interface WebTestClientBuilderCustomizer {

	/**
	 * Customize the given {@code builder}.
	 * @param builder the builder
	 */
	void customize(Builder builder);

}
