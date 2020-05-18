

package org.springframework.boot.context.properties;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;

/**
 * Utility to deduce the {@link PropertySources} to use for configuration binding.
 */
class PropertySourcesDeducer {

	private static final Log logger = LogFactory.getLog(PropertySourcesDeducer.class);

	private final ApplicationContext applicationContext;

	PropertySourcesDeducer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public PropertySources getPropertySources() {
		MutablePropertySources environmentPropertySources = extractEnvironmentPropertySources();
		PropertySourcesPlaceholderConfigurer placeholderConfigurer = getSinglePropertySourcesPlaceholderConfigurer();
		if (placeholderConfigurer == null) {
			Assert.state(environmentPropertySources != null,"Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment");
			return environmentPropertySources;
		}
		PropertySources appliedPropertySources = placeholderConfigurer.getAppliedPropertySources();
		if (environmentPropertySources == null) {
			return appliedPropertySources;
		}
		return merge(environmentPropertySources, appliedPropertySources);
	}

	private MutablePropertySources extractEnvironmentPropertySources() {
		Environment environment = this.applicationContext.getEnvironment();
		if (environment instanceof ConfigurableEnvironment) {
			return ((ConfigurableEnvironment) environment).getPropertySources();
		}
		return null;
	}

	private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
		// Take care not to cause early instantiation of all FactoryBeans
		Map<String, PropertySourcesPlaceholderConfigurer> beans = this.applicationContext.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
		if (beans.size() == 1) {
			return beans.values().iterator().next();
		}
		if (beans.size() > 1 && logger.isWarnEnabled()) {
			logger.warn("Multiple PropertySourcesPlaceholderConfigurer " + "beans registered " + beans.keySet() + ", falling back to Environment");
		}
		return null;
	}

	private PropertySources merge(PropertySources environmentPropertySources,PropertySources appliedPropertySources) {
		FilteredPropertySources filtered = new FilteredPropertySources(appliedPropertySources,PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME);
		return new CompositePropertySources(filtered, environmentPropertySources);
	}

}
