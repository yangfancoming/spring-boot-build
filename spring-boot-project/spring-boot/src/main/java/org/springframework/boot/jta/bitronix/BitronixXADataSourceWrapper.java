

package org.springframework.boot.jta.bitronix;

import javax.sql.XADataSource;

import org.springframework.boot.jdbc.XADataSourceWrapper;

/**
 * {@link XADataSourceWrapper} that uses a Bitronix {@link PoolingDataSourceBean} to wrap
 * a {@link XADataSource}.
 *
 * @author Phillip Webb
 * @since 1.2.0
 */
public class BitronixXADataSourceWrapper implements XADataSourceWrapper {

	@Override
	public PoolingDataSourceBean wrapDataSource(XADataSource dataSource)
			throws Exception {
		PoolingDataSourceBean pool = new PoolingDataSourceBean();
		pool.setDataSource(dataSource);
		return pool;
	}

}
