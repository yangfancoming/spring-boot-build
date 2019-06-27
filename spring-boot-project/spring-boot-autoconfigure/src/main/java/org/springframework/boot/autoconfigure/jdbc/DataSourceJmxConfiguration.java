

package org.springframework.boot.autoconfigure.jdbc;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

/**
 * Configures DataSource related MBeans.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.jmx", name = "enabled", havingValue = "true", matchIfMissing = true)
class DataSourceJmxConfiguration {

	private static final Log logger = LogFactory.getLog(DataSourceJmxConfiguration.class);

	@Configuration
	@ConditionalOnClass(HikariDataSource.class)
	@ConditionalOnSingleCandidate(DataSource.class)
	static class Hikari {

		private final DataSource dataSource;

		private final ObjectProvider<MBeanExporter> mBeanExporter;

		Hikari(DataSource dataSource, ObjectProvider<MBeanExporter> mBeanExporter) {
			this.dataSource = dataSource;
			this.mBeanExporter = mBeanExporter;
		}

		@PostConstruct
		public void validateMBeans() {
			HikariDataSource hikariDataSource = unwrapHikariDataSource();
			if (hikariDataSource != null && hikariDataSource.isRegisterMbeans()) {
				this.mBeanExporter
						.ifUnique((exporter) -> exporter.addExcludedBean("dataSource"));
			}
		}

		private HikariDataSource unwrapHikariDataSource() {
			try {
				return this.dataSource.unwrap(HikariDataSource.class);
			}
			catch (SQLException ex) {
				return null;
			}
		}

	}

	@Configuration
	@ConditionalOnProperty(prefix = "spring.datasource", name = "jmx-enabled")
	@ConditionalOnClass(name = "org.apache.tomcat.jdbc.pool.DataSourceProxy")
	@ConditionalOnSingleCandidate(DataSource.class)
	static class TomcatDataSourceJmxConfiguration {

		@Bean
		@ConditionalOnMissingBean(name = "dataSourceMBean")
		public Object dataSourceMBean(DataSource dataSource) {
			if (dataSource instanceof DataSourceProxy) {
				try {
					return ((DataSourceProxy) dataSource).createPool().getJmxPool();
				}
				catch (SQLException ex) {
					logger.warn("Cannot expose DataSource to JMX (could not connect)");
				}
			}
			return null;
		}

	}

}
