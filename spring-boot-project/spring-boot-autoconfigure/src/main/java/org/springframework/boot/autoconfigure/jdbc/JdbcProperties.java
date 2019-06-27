

package org.springframework.boot.autoconfigure.jdbc;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

/**
 * Configuration properties for JDBC.
 *
 * @author Kazuki Shimizu
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "spring.jdbc")
public class JdbcProperties {

	private final Template template = new Template();

	public Template getTemplate() {
		return this.template;
	}

	/**
	 * {@code JdbcTemplate} settings.
	 */
	public static class Template {

		/**
		 * Number of rows that should be fetched from the database when more rows are
		 * needed. Use -1 to use the JDBC driver's default configuration.
		 */
		private int fetchSize = -1;

		/**
		 * Maximum number of rows. Use -1 to use the JDBC driver's default configuration.
		 */
		private int maxRows = -1;

		/**
		 * Query timeout. Default is to use the JDBC driver's default configuration. If a
		 * duration suffix is not specified, seconds will be used.
		 */
		@DurationUnit(ChronoUnit.SECONDS)
		private Duration queryTimeout;

		public int getFetchSize() {
			return this.fetchSize;
		}

		public void setFetchSize(int fetchSize) {
			this.fetchSize = fetchSize;
		}

		public int getMaxRows() {
			return this.maxRows;
		}

		public void setMaxRows(int maxRows) {
			this.maxRows = maxRows;
		}

		public Duration getQueryTimeout() {
			return this.queryTimeout;
		}

		public void setQueryTimeout(Duration queryTimeout) {
			this.queryTimeout = queryTimeout;
		}

	}

}
