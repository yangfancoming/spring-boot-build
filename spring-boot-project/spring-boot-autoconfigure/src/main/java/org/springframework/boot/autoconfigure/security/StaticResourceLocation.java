

package org.springframework.boot.autoconfigure.security;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Common locations for static resources.
 *
 * @author Phillip Webb
 */
public enum StaticResourceLocation {

	/**
	 * Resources under {@code "/css"}.
	 */
	CSS("/css/**"),

	/**
	 * Resources under {@code "/js"}.
	 */
	JAVA_SCRIPT("/js/**"),

	/**
	 * Resources under {@code "/images"}.
	 */
	IMAGES("/images/**"),

	/**
	 * Resources under {@code "/webjars"}.
	 */
	WEB_JARS("/webjars/**"),

	/**
	 * The {@code "favicon.ico"} resource.
	 */
	FAVICON("/**/favicon.ico");

	private final String[] patterns;

	StaticResourceLocation(String... patterns) {
		this.patterns = patterns;
	}

	public Stream<String> getPatterns() {
		return Arrays.stream(this.patterns);
	}

}
