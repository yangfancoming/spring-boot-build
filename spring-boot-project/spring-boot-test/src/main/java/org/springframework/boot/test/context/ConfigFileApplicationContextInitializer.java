

package org.springframework.boot.test.context;

import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * {@link ApplicationContextInitializer} that can be used with the
 * {@link ContextConfiguration#initializers()} to trigger loading of
 * {@literal application.properties}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see ConfigFileApplicationListener
 */
public class ConfigFileApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		new ConfigFileApplicationListener() {
			public void apply() {
				addPropertySources(applicationContext.getEnvironment(),
						applicationContext);
				addPostProcessors(applicationContext);
			}
		}.apply();
	}

}
