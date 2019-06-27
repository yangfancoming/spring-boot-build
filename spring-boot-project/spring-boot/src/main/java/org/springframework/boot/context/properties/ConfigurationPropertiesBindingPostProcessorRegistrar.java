

package org.springframework.boot.context.properties;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link ImportBeanDefinitionRegistrar} for binding externalized application properties
 * to {@link ConfigurationProperties} beans.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
public class ConfigurationPropertiesBindingPostProcessorRegistrar
		implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		if (!registry.containsBeanDefinition(
				ConfigurationPropertiesBindingPostProcessor.BEAN_NAME)) {
			registerConfigurationPropertiesBindingPostProcessor(registry);
			registerConfigurationBeanFactoryMetadata(registry);
		}
	}

	private void registerConfigurationPropertiesBindingPostProcessor(
			BeanDefinitionRegistry registry) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(ConfigurationPropertiesBindingPostProcessor.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(
				ConfigurationPropertiesBindingPostProcessor.BEAN_NAME, definition);

	}

	private void registerConfigurationBeanFactoryMetadata(
			BeanDefinitionRegistry registry) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(ConfigurationBeanFactoryMetadata.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(ConfigurationBeanFactoryMetadata.BEAN_NAME,
				definition);
	}

}
