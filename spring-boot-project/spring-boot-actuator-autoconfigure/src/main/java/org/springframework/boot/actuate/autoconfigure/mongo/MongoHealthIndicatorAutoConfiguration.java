

package org.springframework.boot.actuate.autoconfigure.mongo;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link MongoHealthIndicator}.
 *
 * @author Christian Dupuis
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnEnabledHealthIndicator("mongo")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter({ MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
		MongoReactiveDataAutoConfiguration.class })
@Import({ MongoReactiveHealthIndicatorConfiguration.class,
		MongoHealthIndicatorConfiguration.class })
public class MongoHealthIndicatorAutoConfiguration {

}
