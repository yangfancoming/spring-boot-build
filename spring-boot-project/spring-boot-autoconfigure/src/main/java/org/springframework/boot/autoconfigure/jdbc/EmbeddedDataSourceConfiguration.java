

package org.springframework.boot.autoconfigure.jdbc;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

/**
 * Configuration for embedded data sources.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @see DataSourceAutoConfiguration
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class EmbeddedDataSourceConfiguration implements BeanClassLoaderAware {

	private EmbeddedDatabase database;

	private ClassLoader classLoader;

	private final DataSourceProperties properties;

	public EmbeddedDataSourceConfiguration(DataSourceProperties properties) {
		this.properties = properties;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Bean
	public EmbeddedDatabase dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseConnection.get(this.classLoader).getType())
				.setName(this.properties.determineDatabaseName());
		this.database = builder.build();
		return this.database;
	}

	@PreDestroy
	public void close() {
		if (this.database != null) {
			this.database.shutdown();
		}
	}

}
