

package org.springframework.boot.autoconfigure.jooq;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jooq.SQLDialect;
import org.jooq.tools.jdbc.JDBCUtils;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * Utility to lookup well known {@link SQLDialect SQLDialects} from a {@link DataSource}.
 *
 * @author Michael Simons
 * @author Lukas Eder
 */
final class SqlDialectLookup {

	private static final Log logger = LogFactory.getLog(SqlDialectLookup.class);

	private SqlDialectLookup() {
	}

	/**
	 * Return the most suitable {@link SQLDialect} for the given {@link DataSource}.
	 * @param dataSource the source {@link DataSource}
	 * @return the most suitable {@link SQLDialect}
	 */
	public static SQLDialect getDialect(DataSource dataSource) {
		if (dataSource == null) {
			return SQLDialect.DEFAULT;
		}
		try {
			String url = JdbcUtils.extractDatabaseMetaData(dataSource, "getURL");
			SQLDialect sqlDialect = JDBCUtils.dialect(url);
			if (sqlDialect != null) {
				return sqlDialect;
			}
		}
		catch (MetaDataAccessException ex) {
			logger.warn("Unable to determine jdbc url from datasource", ex);
		}
		return SQLDialect.DEFAULT;
	}

}
