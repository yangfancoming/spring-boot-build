

package org.springframework.boot.testsupport.runner.classpath;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used in combination with {@link ModifiedClassPathRunner} to exclude entries
 * from the classpath.
 *
 * @author Andy Wilkinson
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ClassPathExclusions {

	/**
	 * One or more Ant-style patterns that identify entries to be excluded from the class
	 * path. Matching is performed against an entry's {@link File#getName() file name}.
	 * For example, to exclude Hibernate Validator from the classpath,
	 * {@code "hibernate-validator-*.jar"} can be used.
	 * @return the exclusion patterns
	 */
	String[] value();

}
