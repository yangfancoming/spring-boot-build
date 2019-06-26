

package org.springframework.boot.system;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.util.ClassUtils;

/**
 * Supported Java versions.
 *
 * @author Oliver Gierke
 * @author Phillip Webb
 * @since 2.0.0
 */
public enum JavaVersion {

	/**
	 * Java 1.8.
	 */
	EIGHT("1.8", "java.util.function.Function"),

	/**
	 * Java 1.9.
	 */
	NINE("1.9", "java.security.cert.URICertStoreParameters");

	private final String name;

	private final boolean available;

	JavaVersion(String name, String className) {
		this.name = name;
		this.available = ClassUtils.isPresent(className, getClass().getClassLoader());
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Returns the {@link JavaVersion} of the current runtime.
	 * @return the {@link JavaVersion}
	 */
	public static JavaVersion getJavaVersion() {
		List<JavaVersion> candidates = Arrays.asList(JavaVersion.values());
		Collections.reverse(candidates);
		for (JavaVersion candidate : candidates) {
			if (candidate.available) {
				return candidate;
			}
		}
		return EIGHT;
	}

	/**
	 * Return if this version is equal to or newer than a given version.
	 * @param version the version to compare
	 * @return {@code true} if this version is equal to or newer than {@code version}
	 */
	public boolean isEqualOrNewerThan(JavaVersion version) {
		return compareTo(version) >= 0;
	}

	/**
	 * Return if this version is older than a given version.
	 * @param version the version to compare
	 * @return {@code true} if this version is older than {@code version}
	 */
	public boolean isOlderThan(JavaVersion version) {
		return compareTo(version) < 0;
	}

}
