

package org.springframework.boot.context.properties.source;

import java.util.function.Predicate;

import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.PropertySource;

/**
 * A source of {@link ConfigurationProperty ConfigurationProperties}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 * @see ConfigurationPropertyName
 * @see OriginTrackedValue
 * @see #getConfigurationProperty(ConfigurationPropertyName)
 */
public interface ConfigurationPropertySource {

	/**
	 * Return a single {@link ConfigurationProperty} from the source or {@code null} if no
	 * property can be found.
	 * @param name the name of the property (must not be {@code null})
	 * @return the associated object or {@code null}.
	 */
	ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name);

	/**
	 * Returns if the source contains any descendants of the specified name. May return
	 * {@link ConfigurationPropertyState#PRESENT} or
	 * {@link ConfigurationPropertyState#ABSENT} if an answer can be determined or
	 * {@link ConfigurationPropertyState#UNKNOWN} if it's not possible to determine a
	 * definitive answer.
	 * @param name the name to check
	 * @return if the source contains any descendants
	 */
	default ConfigurationPropertyState containsDescendantOf(
			ConfigurationPropertyName name) {
		return ConfigurationPropertyState.UNKNOWN;
	}

	/**
	 * Return a filtered variant of this source, containing only names that match the
	 * given {@link Predicate}.
	 * @param filter the filter to match
	 * @return a filtered {@link ConfigurationPropertySource} instance
	 */
	default ConfigurationPropertySource filter(
			Predicate<ConfigurationPropertyName> filter) {
		return new FilteredConfigurationPropertiesSource(this, filter);
	}

	/**
	 * Return a variant of this source that supports name aliases.
	 * @param aliases a function that returns a stream of aliases for any given name
	 * @return a {@link ConfigurationPropertySource} instance supporting name aliases
	 */
	default ConfigurationPropertySource withAliases(
			ConfigurationPropertyNameAliases aliases) {
		return new AliasedConfigurationPropertySource(this, aliases);
	}

	/**
	 * Return the underlying {@link PropertySource}.
	 * @return the underlying property source.
	 */
	Object getUnderlyingSource();

}
