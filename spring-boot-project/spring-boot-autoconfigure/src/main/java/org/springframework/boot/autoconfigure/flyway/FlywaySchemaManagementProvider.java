

package org.springframework.boot.autoconfigure.flyway;

import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;

/**
 * A Flyway {@link SchemaManagementProvider} that determines if the schema is managed by
 * looking at available {@link Flyway} instances.
 *
 * @author Stephane Nicoll
 */
class FlywaySchemaManagementProvider implements SchemaManagementProvider {

	private final List<Flyway> flywayInstances;

	FlywaySchemaManagementProvider(List<Flyway> flywayInstances) {
		this.flywayInstances = flywayInstances;
	}

	@Override
	public SchemaManagement getSchemaManagement(DataSource dataSource) {
		for (Flyway flywayInstance : this.flywayInstances) {
			if (dataSource.equals(flywayInstance.getDataSource())) {
				return SchemaManagement.MANAGED;
			}
		}
		return SchemaManagement.UNMANAGED;
	}

}
