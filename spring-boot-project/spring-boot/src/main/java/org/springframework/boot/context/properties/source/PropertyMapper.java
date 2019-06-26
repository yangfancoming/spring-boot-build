

package org.springframework.boot.context.properties.source;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

/**
 * Strategy used to provide a mapping between a {@link PropertySource} and a
 * {@link ConfigurationPropertySource}.
 * <P>
 * Mappings should be provided for both {@link ConfigurationPropertyName
 * ConfigurationPropertyName} types and {@code String} based names. This allows the
 * {@link SpringConfigurationPropertySource} to first attempt any direct mappings (i.e.
 * map the {@link ConfigurationPropertyName} directly to the {@link PropertySource} name)
 * before falling back to {@link EnumerablePropertySource enumerating} property names,
 * mapping them to a {@link ConfigurationPropertyName} and checking for
 * {@link PropertyMapping#isApplicable(ConfigurationPropertyName) applicability}. See
 * {@link SpringConfigurationPropertySource} for more details.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @see SpringConfigurationPropertySource
 */
interface PropertyMapper {

	PropertyMapping[] NO_MAPPINGS = {};

	/**
	 * Provide mappings from a {@link ConfigurationPropertySource}
	 * {@link ConfigurationPropertyName}.
	 * @param configurationPropertyName the name to map
	 * @return a stream of mappings or {@code Stream#empty()}
	 */
	PropertyMapping[] map(ConfigurationPropertyName configurationPropertyName);

	/**
	 * Provide mappings from a {@link PropertySource} property name.
	 * @param propertySourceName the name to map
	 * @return a stream of mappings or {@code Stream#empty()}
	 */
	PropertyMapping[] map(String propertySourceName);

}
