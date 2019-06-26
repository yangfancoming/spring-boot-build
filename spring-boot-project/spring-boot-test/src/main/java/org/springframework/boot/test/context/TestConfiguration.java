

package org.springframework.boot.test.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

/**
 * {@link Configuration @Configuration} that can be used to define additional beans or
 * customizations for a test. Unlike regular {@code @Configuration} classes the use of
 * {@code @TestConfiguration} does not prevent auto-detection of
 * {@link SpringBootConfiguration @SpringBootConfiguration}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see SpringBootTestContextBootstrapper
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@TestComponent
public @interface TestConfiguration {

	/**
	 * Explicitly specify the name of the Spring bean definition associated with this
	 * Configuration class. See {@link Configuration#value()} for details.
	 * @return the specified bean name, if any
	 */
	@AliasFor(annotation = Configuration.class)
	String value() default "";

}
