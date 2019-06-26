

package org.springframework.boot.testsupport;

import org.junit.AssumptionViolatedException;

import org.springframework.util.ClassUtils;

/**
 * Provides utility methods that allow JUnit tests to {@link org.junit.Assume} certain
 * conditions hold {@code true}. If the assumption fails, it means the test should be
 * skipped.
 *
 * @author Stephane Nicoll
 */
public abstract class Assume {

	public static void javaEight() {
		if (ClassUtils.isPresent("java.security.cert.URICertStoreParameters", null)) {
			throw new AssumptionViolatedException("Assumed Java 8 but got Java 9");
		}
	}

}
