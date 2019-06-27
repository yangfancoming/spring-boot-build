

package org.springframework.boot.context.properties;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.ClassUtils;

/**
 * Exception thrown when {@link ConfigurationProperties @ConfigurationProperties} binding
 * fails.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class ConfigurationPropertiesBindException extends BeanCreationException {

	private final Class<?> beanType;

	private final ConfigurationProperties annotation;

	ConfigurationPropertiesBindException(String beanName, Object bean,
			ConfigurationProperties annotation, Exception cause) {
		super(beanName, getMessage(bean, annotation), cause);
		this.beanType = bean.getClass();
		this.annotation = annotation;
	}

	/**
	 * Return the bean type that was being bound.
	 * @return the bean type
	 */
	public Class<?> getBeanType() {
		return this.beanType;
	}

	/**
	 * Return the configuration properties annotation that triggered the binding.
	 * @return the configuration properties annotation
	 */
	public ConfigurationProperties getAnnotation() {
		return this.annotation;
	}

	private static String getMessage(Object bean, ConfigurationProperties annotation) {
		StringBuilder message = new StringBuilder();
		message.append("Could not bind properties to '"
				+ ClassUtils.getShortName(bean.getClass()) + "' : ");
		message.append("prefix=").append(annotation.prefix());
		message.append(", ignoreInvalidFields=").append(annotation.ignoreInvalidFields());
		message.append(", ignoreUnknownFields=").append(annotation.ignoreUnknownFields());
		return message.toString();
	}

}
