

package org.springframework.boot.jta.narayana;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;

import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.util.Assert;

/**
 * {@link XADataSourceWrapper} that uses {@link NarayanaDataSourceBean} to wrap an
 * {@link XADataSource}.
 *
 * @author Gytis Trikleris
 * @since 1.4.0
 */
public class NarayanaXADataSourceWrapper implements XADataSourceWrapper {

	private final NarayanaRecoveryManagerBean recoveryManager;

	private final NarayanaProperties properties;

	/**
	 * Create a new {@link NarayanaXADataSourceWrapper} instance.
	 * @param recoveryManager the underlying recovery manager
	 * @param properties the Narayana properties
	 */
	public NarayanaXADataSourceWrapper(NarayanaRecoveryManagerBean recoveryManager,
			NarayanaProperties properties) {
		Assert.notNull(recoveryManager, "RecoveryManager must not be null");
		Assert.notNull(properties, "Properties must not be null");
		this.recoveryManager = recoveryManager;
		this.properties = properties;
	}

	@Override
	public DataSource wrapDataSource(XADataSource dataSource) {
		XAResourceRecoveryHelper recoveryHelper = getRecoveryHelper(dataSource);
		this.recoveryManager.registerXAResourceRecoveryHelper(recoveryHelper);
		return new NarayanaDataSourceBean(dataSource);
	}

	private XAResourceRecoveryHelper getRecoveryHelper(XADataSource dataSource) {
		if (this.properties.getRecoveryDbUser() == null
				&& this.properties.getRecoveryDbPass() == null) {
			return new DataSourceXAResourceRecoveryHelper(dataSource);
		}
		return new DataSourceXAResourceRecoveryHelper(dataSource,
				this.properties.getRecoveryDbUser(), this.properties.getRecoveryDbPass());
	}

}
