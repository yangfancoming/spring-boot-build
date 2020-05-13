

package org.springframework.boot;

import org.springframework.core.Ordered;

/**
 * Interface used to indicate that a bean should <em>run</em> when it is contained within a {@link SpringApplication}.
 * Multiple {@link ApplicationRunner} beans can be defined within the same application context and can be ordered using the {@link Ordered} interface or {@link Order @Order} annotation.
 * @since 1.3.0
 * @see CommandLineRunner
 */
@FunctionalInterface
public interface ApplicationRunner {

	/**
	 * Callback used to run the bean.
	 * @param args incoming application arguments
	 * @throws Exception on error
	 */
	void run(ApplicationArguments args) throws Exception;

}
