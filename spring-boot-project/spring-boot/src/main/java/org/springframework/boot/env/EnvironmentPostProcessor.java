

package org.springframework.boot.env;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Allows for customization of the application's {@link Environment} prior to the application context being refreshed.
 * EnvironmentPostProcessor implementations have to be registered in
 * {@code META-INF/spring.factories}, using the fully qualified name of this class as the key.
 * {@code EnvironmentPostProcessor} processors are encouraged to detect whether Spring's {@link org.springframework.core.Ordered Ordered} interface has been implemented or if
 * the @{@link org.springframework.core.annotation.Order Order} annotation is present and to sort instances accordingly if so prior to invocation.
 * @since 1.3.0
 */
@FunctionalInterface
public interface EnvironmentPostProcessor {

	/**
	 * Post-process the given {@code environment}.
	 * @param environment the environment to post-process
	 * @param application the application to which the environment belongs
	 */
	void postProcessEnvironment(ConfigurableEnvironment environment,SpringApplication application);

}
