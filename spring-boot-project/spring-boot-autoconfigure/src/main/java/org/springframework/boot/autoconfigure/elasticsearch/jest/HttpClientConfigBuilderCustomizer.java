

package org.springframework.boot.autoconfigure.elasticsearch.jest;

import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.config.HttpClientConfig.Builder;

/**
 * Callback interface that can be implemented by beans wishing to further customize the
 * {@link HttpClientConfig} via a {@link Builder HttpClientConfig.Builder} whilst
 * retaining default auto-configuration.
 *
 * @author Stephane Nicoll
 * @since 1.5.0
 */
@FunctionalInterface
public interface HttpClientConfigBuilderCustomizer {

	/**
	 * Customize the {@link Builder}.
	 * @param builder the builder to customize
	 */
	void customize(Builder builder);

}
