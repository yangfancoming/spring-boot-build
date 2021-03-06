

package org.springframework.boot.actuate.endpoint.web.test;

import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phillip Webb
 */
@FunctionalInterface
interface ContextFactory {

	ConfigurableApplicationContext createContext(List<Class<?>> configurationClasses);

}
