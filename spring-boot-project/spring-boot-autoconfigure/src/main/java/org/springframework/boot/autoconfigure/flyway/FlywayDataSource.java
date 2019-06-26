

package org.springframework.boot.autoconfigure.flyway;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Qualifier annotation for a DataSource to be injected in to Flyway. If used for a second
 * data source, the other (main) one would normally be marked as {@code @Primary}.
 *
 * @author Dave Syer
 * @since 1.1.0
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE,
		ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface FlywayDataSource {

}
