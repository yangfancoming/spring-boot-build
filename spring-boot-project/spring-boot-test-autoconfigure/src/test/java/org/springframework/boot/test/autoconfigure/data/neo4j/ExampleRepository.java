

package org.springframework.boot.test.autoconfigure.data.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * Example repository used with {@link DataNeo4jTest} tests.
 *
 * @author Eddú Meléndez
 */
public interface ExampleRepository extends Neo4jRepository<ExampleGraph, Long> {

}
