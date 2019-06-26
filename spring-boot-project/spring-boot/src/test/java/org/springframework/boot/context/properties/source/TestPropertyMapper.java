

package org.springframework.boot.context.properties.source;

import java.util.Collections;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Test {@link PropertyMapper} implementation.
 */
class TestPropertyMapper implements PropertyMapper {

	private MultiValueMap<String, PropertyMapping> fromSource = new LinkedMultiValueMap<>();

	private MultiValueMap<ConfigurationPropertyName, PropertyMapping> fromConfig = new LinkedMultiValueMap<>();

	public void addFromPropertySource(String from, String... to) {
		for (String configurationPropertyName : to) {
			this.fromSource.add(from, new PropertyMapping(from,
					ConfigurationPropertyName.of(configurationPropertyName)));
		}
	}

	public void addFromConfigurationProperty(ConfigurationPropertyName from,
			String... to) {
		for (String propertySourceName : to) {
			this.fromConfig.add(from, new PropertyMapping(propertySourceName, from));
		}
	}

	@Override
	public PropertyMapping[] map(String propertySourceName) {
		return this.fromSource.getOrDefault(propertySourceName, Collections.emptyList())
				.toArray(new PropertyMapping[0]);
	}

	@Override
	public PropertyMapping[] map(ConfigurationPropertyName configurationPropertyName) {
		return this.fromConfig
				.getOrDefault(configurationPropertyName, Collections.emptyList())
				.toArray(new PropertyMapping[0]);
	}

}
