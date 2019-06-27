

package org.springframework.boot.autoconfigure.data.elasticsearch;

import java.util.Properties;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} for Elasticsearch.
 *
 * @author Artur Konczak
 * @author Mohsin Husen
 * @author Andy Wilkinson
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({ Client.class, TransportClientFactoryBean.class })
@ConditionalOnProperty(prefix = "spring.data.elasticsearch", name = "cluster-nodes", matchIfMissing = false)
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchAutoConfiguration {

	private final ElasticsearchProperties properties;

	public ElasticsearchAutoConfiguration(ElasticsearchProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public TransportClient elasticsearchClient() throws Exception {
		TransportClientFactoryBean factory = new TransportClientFactoryBean();
		factory.setClusterNodes(this.properties.getClusterNodes());
		factory.setProperties(createProperties());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	private Properties createProperties() {
		Properties properties = new Properties();
		properties.put("cluster.name", this.properties.getClusterName());
		properties.putAll(this.properties.getProperties());
		return properties;
	}

}
