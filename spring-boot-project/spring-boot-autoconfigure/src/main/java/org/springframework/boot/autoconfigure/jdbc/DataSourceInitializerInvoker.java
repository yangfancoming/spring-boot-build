

package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

/**
 * Bean to handle {@link DataSource} initialization by running {@literal schema-*.sql} on
 * {@link InitializingBean#afterPropertiesSet()} and {@literal data-*.sql} SQL scripts on
 * a {@link DataSourceSchemaCreatedEvent}.
 *
 * @author Stephane Nicoll
 * @see DataSourceAutoConfiguration
 */
class DataSourceInitializerInvoker
		implements ApplicationListener<DataSourceSchemaCreatedEvent>, InitializingBean {

	private static final Log logger = LogFactory
			.getLog(DataSourceInitializerInvoker.class);

	private final ObjectProvider<DataSource> dataSource;

	private final DataSourceProperties properties;

	private final ApplicationContext applicationContext;

	private DataSourceInitializer dataSourceInitializer;

	private boolean initialized;

	DataSourceInitializerInvoker(ObjectProvider<DataSource> dataSource,
			DataSourceProperties properties, ApplicationContext applicationContext) {
		this.dataSource = dataSource;
		this.properties = properties;
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		DataSourceInitializer initializer = getDataSourceInitializer();
		if (initializer != null) {
			boolean schemaCreated = this.dataSourceInitializer.createSchema();
			if (schemaCreated) {
				initialize(initializer);
			}
		}
	}

	private void initialize(DataSourceInitializer initializer) {
		try {
			this.applicationContext.publishEvent(
					new DataSourceSchemaCreatedEvent(initializer.getDataSource()));
			// The listener might not be registered yet, so don't rely on it.
			if (!this.initialized) {
				this.dataSourceInitializer.initSchema();
				this.initialized = true;
			}
		}
		catch (IllegalStateException ex) {
			logger.warn("Could not send event to complete DataSource initialization ("
					+ ex.getMessage() + ")");
		}
	}

	@Override
	public void onApplicationEvent(DataSourceSchemaCreatedEvent event) {
		// NOTE the event can happen more than once and
		// the event datasource is not used here
		DataSourceInitializer initializer = getDataSourceInitializer();
		if (!this.initialized && initializer != null) {
			initializer.initSchema();
			this.initialized = true;
		}
	}

	private DataSourceInitializer getDataSourceInitializer() {
		if (this.dataSourceInitializer == null) {
			DataSource ds = this.dataSource.getIfUnique();
			if (ds != null) {
				this.dataSourceInitializer = new DataSourceInitializer(ds,
						this.properties, this.applicationContext);
			}
		}
		return this.dataSourceInitializer;
	}

}
