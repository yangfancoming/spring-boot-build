

package org.springframework.boot.autoconfigure.jms.artemis;

/**
 * Define the mode in which Artemis can operate.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @since 1.3.0
 */
public enum ArtemisMode {

	/**
	 * Connect to a broker using the native Artemis protocol (i.e. netty).
	 */
	NATIVE,

	/**
	 * Embed (i.e. start) the broker in the application.
	 */
	EMBEDDED

}
