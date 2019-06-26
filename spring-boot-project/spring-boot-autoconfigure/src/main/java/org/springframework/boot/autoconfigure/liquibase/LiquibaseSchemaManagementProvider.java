

package org.springframework.boot.autoconfigure.liquibase;

import java.util.List;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;

/**
 * A Liquibase {@link SchemaManagementProvider} that determines if the schema is managed
 * by looking at available {@link SpringLiquibase} instances.
 *
 * @author Stephane Nicoll
 */
class LiquibaseSchemaManagementProvider implements SchemaManagementProvider {

	private final List<SpringLiquibase> liquibaseInstances;

	LiquibaseSchemaManagementProvider(List<SpringLiquibase> liquibases) {
		this.liquibaseInstances = liquibases;
	}

	@Override
	public SchemaManagement getSchemaManagement(DataSource dataSource) {
		for (SpringLiquibase liquibaseInstance : this.liquibaseInstances) {
			if (dataSource.equals(liquibaseInstance.getDataSource())) {
				return SchemaManagement.MANAGED;
			}
		}
		return SchemaManagement.UNMANAGED;
	}

}
