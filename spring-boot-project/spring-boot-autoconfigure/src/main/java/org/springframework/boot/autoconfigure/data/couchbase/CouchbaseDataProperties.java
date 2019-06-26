

package org.springframework.boot.autoconfigure.data.couchbase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.couchbase.core.query.Consistency;

/**
 * Configuration properties for Spring Data Couchbase.
 *
 * @author Stephane Nicoll
 * @since 1.4.0
 */
@ConfigurationProperties(prefix = "spring.data.couchbase")
public class CouchbaseDataProperties {

	/**
	 * Automatically create views and indexes. Use the meta-data provided by
	 * "@ViewIndexed", "@N1qlPrimaryIndexed" and "@N1qlSecondaryIndexed".
	 */
	private boolean autoIndex;

	/**
	 * Consistency to apply by default on generated queries.
	 */
	private Consistency consistency = Consistency.READ_YOUR_OWN_WRITES;

	public boolean isAutoIndex() {
		return this.autoIndex;
	}

	public void setAutoIndex(boolean autoIndex) {
		this.autoIndex = autoIndex;
	}

	public Consistency getConsistency() {
		return this.consistency;
	}

	public void setConsistency(Consistency consistency) {
		this.consistency = consistency;
	}

}
