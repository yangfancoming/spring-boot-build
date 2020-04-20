

package org.springframework.boot.cli.compiler.grape;

/**
 * Reports progress on a dependency resolution operation.
 *
 * @author Andy Wilkinson
 */
@FunctionalInterface
interface ProgressReporter {

	/**
	 * Notification that the operation has completed.
	 */
	void finished();

}
