

package org.springframework.boot.actuate.neo4j;

import java.util.Collections;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * {@link HealthIndicator} that tests the status of a Neo4j by executing a Cypher
 * statement.
 *
 * @author Eric Spiegelberg
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class Neo4jHealthIndicator extends AbstractHealthIndicator {

	/**
	 * The Cypher statement used to verify Neo4j is up.
	 */
	static final String CYPHER = "match (n) return count(n) as nodes";

	private final SessionFactory sessionFactory;

	/**
	 * Create a new {@link Neo4jHealthIndicator} using the specified
	 * {@link SessionFactory}.
	 * @param sessionFactory the SessionFactory
	 */
	public Neo4jHealthIndicator(SessionFactory sessionFactory) {
		super("Neo4J health check failed");
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		Session session = this.sessionFactory.openSession();
		extractResult(session, builder);
	}

	/**
	 * Provide health details using the specified {@link Session} and {@link Builder
	 * Builder}.
	 * @param session the session to use to execute a cypher statement
	 * @param builder the builder to add details to
	 * @throws Exception if getting health details failed
	 */
	protected void extractResult(Session session, Health.Builder builder)
			throws Exception {
		Result result = session.query(CYPHER, Collections.emptyMap());
		builder.up().withDetail("nodes",
				result.queryResults().iterator().next().get("nodes"));
	}

}
