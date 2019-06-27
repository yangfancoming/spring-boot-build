

package org.springframework.boot.autoconfigure.freemarker;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * Configuration for FreeMarker when used in a non-web context.
 *
 * @author Brian Clozel
 * @author Andy Wilkinson
 */
@Configuration
@ConditionalOnNotWebApplication
class FreeMarkerNonWebConfiguration extends AbstractFreeMarkerConfiguration {

	FreeMarkerNonWebConfiguration(FreeMarkerProperties properties) {
		super(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
		applyProperties(freeMarkerFactoryBean);
		return freeMarkerFactoryBean;
	}

}
