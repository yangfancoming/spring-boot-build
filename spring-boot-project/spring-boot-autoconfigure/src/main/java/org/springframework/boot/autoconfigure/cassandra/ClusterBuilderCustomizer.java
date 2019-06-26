

package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

/**
 * Callback interface that can be implemented by beans wishing to customize the
 * {@link Cluster} via a {@link Builder Cluster.Builder} whilst retaining default
 * auto-configuration.
 *
 * @author Eddú Meléndez
 * @since 1.5.0
 */
@FunctionalInterface
public interface ClusterBuilderCustomizer {

	/**
	 * Customize the {@link Builder}.
	 * @param clusterBuilder the builder to customize
	 */
	void customize(Builder clusterBuilder);

}
