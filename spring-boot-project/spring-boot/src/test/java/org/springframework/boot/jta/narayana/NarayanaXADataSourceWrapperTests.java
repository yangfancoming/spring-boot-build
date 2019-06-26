

package org.springframework.boot.jta.narayana;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link NarayanaXADataSourceWrapper}.
 *
 * @author Gytis Trikleris
 */
public class NarayanaXADataSourceWrapperTests {

	private XADataSource dataSource = mock(XADataSource.class);

	private NarayanaRecoveryManagerBean recoveryManager = mock(
			NarayanaRecoveryManagerBean.class);

	private NarayanaProperties properties = mock(NarayanaProperties.class);

	private NarayanaXADataSourceWrapper wrapper = new NarayanaXADataSourceWrapper(
			this.recoveryManager, this.properties);

	@Test
	public void wrap() {
		DataSource wrapped = this.wrapper.wrapDataSource(this.dataSource);
		assertThat(wrapped).isInstanceOf(NarayanaDataSourceBean.class);
		verify(this.recoveryManager, times(1)).registerXAResourceRecoveryHelper(
				any(DataSourceXAResourceRecoveryHelper.class));
		verify(this.properties, times(1)).getRecoveryDbUser();
		verify(this.properties, times(1)).getRecoveryDbPass();
	}

	@Test
	public void wrapWithCredentials() {
		given(this.properties.getRecoveryDbUser()).willReturn("userName");
		given(this.properties.getRecoveryDbPass()).willReturn("password");
		DataSource wrapped = this.wrapper.wrapDataSource(this.dataSource);
		assertThat(wrapped).isInstanceOf(NarayanaDataSourceBean.class);
		verify(this.recoveryManager, times(1)).registerXAResourceRecoveryHelper(
				any(DataSourceXAResourceRecoveryHelper.class));
		verify(this.properties, times(2)).getRecoveryDbUser();
		verify(this.properties, times(1)).getRecoveryDbPass();
	}

}
