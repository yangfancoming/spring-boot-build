

package org.springframework.boot.test.autoconfigure.data.neo4j;

import org.neo4j.ogm.session.Session;

import org.springframework.stereotype.Service;

/**
 * Example service used with {@link DataNeo4jTest} tests.
 *
 * @author Eddú Meléndez
 */
@Service
public class ExampleService {

	private final Session session;

	public ExampleService(Session session) {
		this.session = session;
	}

	public boolean hasNode(Class<?> clazz) {
		return this.session.countEntitiesOfType(clazz) == 1;
	}

}
