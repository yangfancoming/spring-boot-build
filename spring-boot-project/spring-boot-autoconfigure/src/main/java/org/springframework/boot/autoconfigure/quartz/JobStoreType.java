

package org.springframework.boot.autoconfigure.quartz;

/**
 * Define the supported Quartz {@code JobStore}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public enum JobStoreType {

	/**
	 * Store jobs in memory.
	 */
	MEMORY,

	/**
	 * Store jobs in the database.
	 */
	JDBC

}
