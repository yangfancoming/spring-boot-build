

package org.springframework.boot.actuate.autoconfigure.neo4j;

import java.util.Map;

import org.neo4j.ogm.session.SessionFactory;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.neo4j.Neo4jHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link Neo4jHealthIndicator}.
 *
 * @author Eric Spiegelberg
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(SessionFactory.class)
@ConditionalOnBean(SessionFactory.class)
@ConditionalOnEnabledHealthIndicator("neo4j")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(Neo4jDataAutoConfiguration.class)
public class Neo4jHealthIndicatorAutoConfiguration extends
		CompositeHealthIndicatorConfiguration<Neo4jHealthIndicator, SessionFactory> {

	private final Map<String, SessionFactory> sessionFactories;

	public Neo4jHealthIndicatorAutoConfiguration(
			Map<String, SessionFactory> sessionFactories) {
		this.sessionFactories = sessionFactories;
	}

	@Bean
	@ConditionalOnMissingBean(name = "neo4jHealthIndicator")
	public HealthIndicator neo4jHealthIndicator() {
		return createHealthIndicator(this.sessionFactories);
	}

}
