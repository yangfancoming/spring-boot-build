

package org.springframework.boot.web.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Value;

/**
 * Annotation at the field or method/constructor parameter level that injects the HTTP
 * port that got allocated at runtime. Provides a convenient alternative for
 * <code>&#064;Value(&quot;${local.server.port}&quot;)</code>.
 *
 * @author Anand Shah
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
		ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Value("${local.server.port}")
public @interface LocalServerPort {

}
