

package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;

/**
 * A {@link SchemaManagementProvider} that invokes a configurable number of
 * {@link SchemaManagementProvider} instances for embedded data sources only.
 *
 * @author Stephane Nicoll
 */
class HibernateDefaultDdlAutoProvider implements SchemaManagementProvider {

	private final List<SchemaManagementProvider> providers;

	HibernateDefaultDdlAutoProvider(List<SchemaManagementProvider> providers) {
		this.providers = providers;
	}

	public String getDefaultDdlAuto(DataSource dataSource) {
		if (!EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
			return "none";
		}
		SchemaManagement schemaManagement = getSchemaManagement(dataSource);
		if (SchemaManagement.MANAGED.equals(schemaManagement)) {
			return "none";
		}
		return "create-drop";

	}

	@Override
	public SchemaManagement getSchemaManagement(DataSource dataSource) {
		for (SchemaManagementProvider provider : this.providers) {
			SchemaManagement schemaManagement = provider.getSchemaManagement(dataSource);
			if (SchemaManagement.MANAGED.equals(schemaManagement)) {
				return schemaManagement;
			}
		}
		return SchemaManagement.UNMANAGED;
	}

}
