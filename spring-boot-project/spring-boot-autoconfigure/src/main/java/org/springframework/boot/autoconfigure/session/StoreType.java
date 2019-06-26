

package org.springframework.boot.autoconfigure.session;

/**
 * Supported Spring Session data store types.
 *
 * @author Tommy Ludwig
 * @author Eddú Meléndez
 * @author Vedran Pavic
 * @since 1.4.0
 */
public enum StoreType {

	/**
	 * Redis backed sessions.
	 */
	REDIS,

	/**
	 * MongoDB backed sessions.
	 */
	MONGODB,

	/**
	 * JDBC backed sessions.
	 */
	JDBC,

	/**
	 * Hazelcast backed sessions.
	 */
	HAZELCAST,

	/**
	 * No session data-store.
	 */
	NONE

}
