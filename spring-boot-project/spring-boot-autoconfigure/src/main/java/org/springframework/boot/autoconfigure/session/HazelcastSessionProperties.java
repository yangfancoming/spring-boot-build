

package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.session.hazelcast.HazelcastFlushMode;

/**
 * Configuration properties for Hazelcast backed Spring Session.
 *
 * @author Vedran Pavic
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "spring.session.hazelcast")
public class HazelcastSessionProperties {

	/**
	 * Name of the map used to store sessions.
	 */
	private String mapName = "spring:session:sessions";

	/**
	 * Sessions flush mode.
	 */
	private HazelcastFlushMode flushMode = HazelcastFlushMode.ON_SAVE;

	public String getMapName() {
		return this.mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public HazelcastFlushMode getFlushMode() {
		return this.flushMode;
	}

	public void setFlushMode(HazelcastFlushMode flushMode) {
		this.flushMode = flushMode;
	}

}
