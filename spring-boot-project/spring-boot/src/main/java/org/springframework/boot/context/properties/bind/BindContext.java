

package org.springframework.boot.context.properties.bind;

import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;

/**
 * Context information for use by {@link BindHandler BindHandlers}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 */
public interface BindContext {

	/**
	 * Return the current depth of the binding. Root binding starts with a depth of
	 * {@code 0}. Each subsequent property binding increases the depth by {@code 1}.
	 * @return the depth of the current binding
	 */
	int getDepth();

	/**
	 * Return an {@link Iterable} of the {@link ConfigurationPropertySource sources} being
	 * used by the {@link Binder}.
	 * @return the sources
	 */
	Iterable<ConfigurationPropertySource> getSources();

	/**
	 * Return the {@link ConfigurationProperty} actually being bound or {@code null} if
	 * the property has not yet been determined.
	 * @return the configuration property (may be {@code null}).
	 */
	ConfigurationProperty getConfigurationProperty();

}
