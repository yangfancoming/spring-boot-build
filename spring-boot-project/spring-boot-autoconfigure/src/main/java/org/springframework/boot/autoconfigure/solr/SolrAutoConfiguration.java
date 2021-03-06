

package org.springframework.boot.autoconfigure.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Solr 4.x.
 *
 * @author Christoph Strobl
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({ HttpSolrClient.class, CloudSolrClient.class })
@EnableConfigurationProperties(SolrProperties.class)
public class SolrAutoConfiguration {

	private final SolrProperties properties;

	private SolrClient solrClient;

	public SolrAutoConfiguration(SolrProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public SolrClient solrClient() {
		this.solrClient = createSolrClient();
		return this.solrClient;
	}

	private SolrClient createSolrClient() {
		if (StringUtils.hasText(this.properties.getZkHost())) {
			return new CloudSolrClient.Builder().withZkHost(this.properties.getZkHost())
					.build();
		}
		return new HttpSolrClient.Builder(this.properties.getHost()).build();
	}

}
