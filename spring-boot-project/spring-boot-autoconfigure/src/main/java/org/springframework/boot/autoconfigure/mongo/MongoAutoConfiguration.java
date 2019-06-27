

package org.springframework.boot.autoconfigure.mongo;

import javax.annotation.PreDestroy;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Mongo.
 *
 * @author Dave Syer
 * @author Oliver Gierke
 * @author Phillip Webb
 * @author Mark Paluch
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(MongoProperties.class)
@ConditionalOnMissingBean(type = "org.springframework.data.mongodb.MongoDbFactory")
public class MongoAutoConfiguration {

	private final MongoClientOptions options;

	private final MongoClientFactory factory;

	private MongoClient mongo;

	public MongoAutoConfiguration(MongoProperties properties,
			ObjectProvider<MongoClientOptions> options, Environment environment) {
		this.options = options.getIfAvailable();
		this.factory = new MongoClientFactory(properties, environment);
	}

	@PreDestroy
	public void close() {
		if (this.mongo != null) {
			this.mongo.close();
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public MongoClient mongo() {
		this.mongo = this.factory.createMongoClient(this.options);
		return this.mongo;
	}

}
