

package org.springframework.boot.testsupport.testcontainers;

import org.testcontainers.containers.GenericContainer;

/**
 * A {@link GenericContainer} for Redis.
 *
 * @author Andy Wilkinson
 * @author Madhura Bhave
 */
public class RedisContainer extends Container {

	public RedisContainer() {
		super("redis:4.0.6", 6379);
	}

}
