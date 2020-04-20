

package org.springframework.boot.cli.compiler;

/**
 * The scope in which a groovy compiler operates.
 *
 * @author Phillip Webb
 */
public enum GroovyCompilerScope {

	/**
	 * Default scope, exposes groovy.jar (loaded from the parent) and the shared cli
	 * package (loaded via groovy classloader).
	 */
	DEFAULT,

	/**
	 * Extension scope, allows full access to internal CLI classes.
	 */
	EXTENSION

}
