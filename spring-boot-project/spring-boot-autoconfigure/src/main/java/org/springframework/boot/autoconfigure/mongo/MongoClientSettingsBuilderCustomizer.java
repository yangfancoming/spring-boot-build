

package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.async.client.MongoClientSettings.Builder;

/**
 * Callback interface that can be implemented by beans wishing to customize the
 * {@link com.mongodb.async.client.MongoClientSettings} via a {@link Builder
 * MongoClientSettings.Builder} whilst retaining default auto-configuration.
 *
 * @author Mark Paluch
 * @since 2.0.0
 */
@FunctionalInterface
public interface MongoClientSettingsBuilderCustomizer {

	/**
	 * Customize the {@link Builder}.
	 * @param clientSettingsBuilder the builder to customize
	 */
	void customize(Builder clientSettingsBuilder);

}
