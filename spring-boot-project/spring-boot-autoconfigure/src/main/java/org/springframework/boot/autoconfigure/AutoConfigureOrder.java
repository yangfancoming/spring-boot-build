

package org.springframework.boot.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Auto-configuration specific variant of Spring Framework's {@link Order} annotation.
 * Allows auto-configuration classes to be ordered among themselves without affecting the
 * order of configuration classes passed to
 * {@link AnnotationConfigApplicationContext#register(Class...)}.
 *
 * @author Andy Wilkinson
 * @since 1.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface AutoConfigureOrder {

	int DEFAULT_ORDER = 0;

	/**
	 * The order value. Default is {@code 0}.
	 * @see Ordered#getOrder()
	 * @return the order value
	 */
	int value() default DEFAULT_ORDER;

}
