

package org.springframework.boot.autoconfigure.mongo.embedded;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.flapdoodle.embed.mongo.distribution.Feature;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Embedded Mongo.
 *
 * @author Andy Wilkinson
 * @author Yogesh Lonkar
 * @since 1.3.0
 */
@ConfigurationProperties(prefix = "spring.mongodb.embedded")
public class EmbeddedMongoProperties {

	/**
	 * Version of Mongo to use.
	 */
	private String version = "3.2.2";

	private final Storage storage = new Storage();

	/**
	 * Comma-separated list of features to enable.
	 */
	private Set<Feature> features = new HashSet<>(
			Collections.singletonList(Feature.SYNC_DELAY));

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<Feature> getFeatures() {
		return this.features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public static class Storage {

		/**
		 * Maximum size of the oplog, in megabytes.
		 */
		private Integer oplogSize;

		/**
		 * Name of the replica set.
		 */
		private String replSetName;

		/**
		 * Directory used for data storage.
		 */
		private String databaseDir;

		public Integer getOplogSize() {
			return this.oplogSize;
		}

		public void setOplogSize(Integer oplogSize) {
			this.oplogSize = oplogSize;
		}

		public String getReplSetName() {
			return this.replSetName;
		}

		public void setReplSetName(String replSetName) {
			this.replSetName = replSetName;
		}

		public String getDatabaseDir() {
			return this.databaseDir;
		}

		public void setDatabaseDir(String databaseDir) {
			this.databaseDir = databaseDir;
		}

	}

}
