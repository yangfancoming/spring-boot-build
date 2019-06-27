

package org.springframework.boot.actuate.autoconfigure.mongo;

import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.health.CompositeReactiveHealthIndicatorConfiguration;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.mongo.MongoReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * Configuration for {@link MongoReactiveHealthIndicator}.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass(ReactiveMongoTemplate.class)
@ConditionalOnBean(ReactiveMongoTemplate.class)
class MongoReactiveHealthIndicatorConfiguration extends
		CompositeReactiveHealthIndicatorConfiguration<MongoReactiveHealthIndicator, ReactiveMongoTemplate> {

	private final Map<String, ReactiveMongoTemplate> reactiveMongoTemplate;

	MongoReactiveHealthIndicatorConfiguration(
			Map<String, ReactiveMongoTemplate> reactiveMongoTemplate) {
		this.reactiveMongoTemplate = reactiveMongoTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(name = "mongoHealthIndicator")
	public ReactiveHealthIndicator mongoHealthIndicator() {
		return createHealthIndicator(this.reactiveMongoTemplate);
	}

}
