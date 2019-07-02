

package org.springframework.boot.test.autoconfigure.data.neo4j;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * Example graph used with {@link DataNeo4jTest} tests.
 *
 * @author Eddú Meléndez
 */
@NodeEntity
public class ExampleGraph {

	@Id
	@GeneratedValue
	private Long id;

	@Property
	private String description;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
