

package org.springframework.boot.testsupport.runner.classpath;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used in combination with {@link ModifiedClassPathRunner} to override entries
 * on the classpath.
 *
 * @author Andy Wilkinson
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ClassPathOverrides {

	/**
	 * One or more sets of Maven coordinates ({@code groupId:artifactId:version}) to be
	 * added to the classpath. The additions will take precedence over any existing
	 * classes on the classpath.
	 * @return the coordinates
	 */
	String[] value();

}
