package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import reactor.core.publisher.Flux;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.ReactiveSession;
import org.springframework.data.cassandra.ReactiveSessionFactory;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.session.DefaultBridgedReactiveSession;
import org.springframework.data.cassandra.core.cql.session.DefaultReactiveSessionFactory;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's reactive Cassandra support.
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass({ Cluster.class, ReactiveCassandraTemplate.class, Flux.class })
@ConditionalOnBean(Session.class)
@AutoConfigureAfter(CassandraDataAutoConfiguration.class)
public class CassandraReactiveDataAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ReactiveSession reactiveCassandraSession(Session session) {
		return new DefaultBridgedReactiveSession(session);
	}

	@Bean
	public ReactiveSessionFactory reactiveCassandraSessionFactory(ReactiveSession reactiveCassandraSession) {
		return new DefaultReactiveSessionFactory(reactiveCassandraSession);
	}

	@Bean
	@ConditionalOnMissingBean
	public ReactiveCassandraTemplate reactiveCassandraTemplate(ReactiveSession reactiveCassandraSession, CassandraConverter converter) {
		return new ReactiveCassandraTemplate(reactiveCassandraSession, converter);
	}
}
