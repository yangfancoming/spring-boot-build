

package org.springframework.boot.autoconfigure.data.couchbase;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration.CouchbaseConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;

/**
 * Adapt the core Couchbase configuration to an expected {@link CouchbaseConfigurer} if
 * necessary.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass(CouchbaseConfigurer.class)
@ConditionalOnBean(CouchbaseConfiguration.class)
class CouchbaseConfigurerAdapterConfiguration {

	private final CouchbaseConfiguration configuration;

	CouchbaseConfigurerAdapterConfiguration(CouchbaseConfiguration configuration) {
		this.configuration = configuration;
	}

	@Bean
	@ConditionalOnMissingBean
	public CouchbaseConfigurer springBootCouchbaseConfigurer() throws Exception {
		return new SpringBootCouchbaseConfigurer(
				this.configuration.couchbaseEnvironment(),
				this.configuration.couchbaseCluster(),
				this.configuration.couchbaseClusterInfo(),
				this.configuration.couchbaseClient());
	}

}
