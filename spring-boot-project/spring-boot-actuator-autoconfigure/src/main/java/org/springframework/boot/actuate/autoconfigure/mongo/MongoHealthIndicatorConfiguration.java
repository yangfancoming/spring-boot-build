

package org.springframework.boot.actuate.autoconfigure.mongo;

import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration for {@link MongoHealthIndicator}.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnBean(MongoTemplate.class)
class MongoHealthIndicatorConfiguration extends
		CompositeHealthIndicatorConfiguration<MongoHealthIndicator, MongoTemplate> {

	private final Map<String, MongoTemplate> mongoTemplates;

	MongoHealthIndicatorConfiguration(Map<String, MongoTemplate> mongoTemplates) {
		this.mongoTemplates = mongoTemplates;
	}

	@Bean
	@ConditionalOnMissingBean(name = "mongoHealthIndicator")
	public HealthIndicator mongoHealthIndicator() {
		return createHealthIndicator(this.mongoTemplates);
	}

}
