

package org.springframework.boot.actuate.autoconfigure.solr;

import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.solr.SolrHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link SolrHealthIndicator}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(SolrClient.class)
@ConditionalOnBean(SolrClient.class)
@ConditionalOnEnabledHealthIndicator("solr")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(SolrAutoConfiguration.class)
public class SolrHealthIndicatorAutoConfiguration
		extends CompositeHealthIndicatorConfiguration<SolrHealthIndicator, SolrClient> {

	private final Map<String, SolrClient> solrClients;

	public SolrHealthIndicatorAutoConfiguration(Map<String, SolrClient> solrClients) {
		this.solrClients = solrClients;
	}

	@Bean
	@ConditionalOnMissingBean(name = "solrHealthIndicator")
	public HealthIndicator solrHealthIndicator() {
		return createHealthIndicator(this.solrClients);
	}

}
