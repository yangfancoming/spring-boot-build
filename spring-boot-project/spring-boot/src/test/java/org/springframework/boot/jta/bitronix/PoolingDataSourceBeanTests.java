

package org.springframework.boot.jta.bitronix;

import java.sql.Connection;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import bitronix.tm.TransactionManagerServices;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link PoolingDataSourceBean}.
 *
 * @author Phillip Webb
 */
public class PoolingDataSourceBeanTests {

	private PoolingDataSourceBean bean = new PoolingDataSourceBean();

	@Test
	public void sensibleDefaults() {
		assertThat(this.bean.getMaxPoolSize()).isEqualTo(10);
		assertThat(this.bean.getAutomaticEnlistingEnabled()).isTrue();
		assertThat(this.bean.isEnableJdbc4ConnectionTest()).isTrue();
	}

	@Test
	public void setsUniqueNameIfNull() throws Exception {
		this.bean.setBeanName("beanName");
		this.bean.afterPropertiesSet();
		assertThat(this.bean.getUniqueName()).isEqualTo("beanName");
	}

	@Test
	public void doesNotSetUniqueNameIfNotNull() throws Exception {
		this.bean.setBeanName("beanName");
		this.bean.setUniqueName("un");
		this.bean.afterPropertiesSet();
		assertThat(this.bean.getUniqueName()).isEqualTo("un");
	}

	@Test
	public void setDataSource() throws Exception {
		XADataSource dataSource = mock(XADataSource.class);
		XAConnection xaConnection = mock(XAConnection.class);
		Connection connection = mock(Connection.class);
		given(dataSource.getXAConnection()).willReturn(xaConnection);
		given(xaConnection.getConnection()).willReturn(connection);
		this.bean.setDataSource(dataSource);
		this.bean.setBeanName("beanName");
		this.bean.afterPropertiesSet();
		this.bean.init();
		this.bean.createPooledConnection(dataSource, this.bean);
		verify(dataSource).getXAConnection();
		TransactionManagerServices.getTaskScheduler().shutdown();
	}

}
