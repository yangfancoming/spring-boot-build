

package org.springframework.boot.jdbc.metadata;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CommonsDbcp2DataSourcePoolMetadata}.
 *
 * @author Stephane Nicoll
 */
public class CommonsDbcp2DataSourcePoolMetadataTests
		extends AbstractDataSourcePoolMetadataTests<CommonsDbcp2DataSourcePoolMetadata> {

	private CommonsDbcp2DataSourcePoolMetadata dataSourceMetadata;

	@Before
	public void setup() {
		this.dataSourceMetadata = createDataSourceMetadata(0, 2);
	}

	@Override
	protected CommonsDbcp2DataSourcePoolMetadata getDataSourceMetadata() {
		return this.dataSourceMetadata;
	}

	@Test
	public void getPoolUsageWithNoCurrent() {
		CommonsDbcp2DataSourcePoolMetadata dsm = new CommonsDbcp2DataSourcePoolMetadata(
				createDataSource()) {
			@Override
			public Integer getActive() {
				return null;
			}
		};
		assertThat(dsm.getUsage()).isNull();
	}

	@Test
	public void getPoolUsageWithNoMax() {
		CommonsDbcp2DataSourcePoolMetadata dsm = new CommonsDbcp2DataSourcePoolMetadata(
				createDataSource()) {
			@Override
			public Integer getMax() {
				return null;
			}
		};
		assertThat(dsm.getUsage()).isNull();
	}

	@Test
	public void getPoolUsageWithUnlimitedPool() {
		DataSourcePoolMetadata unlimitedDataSource = createDataSourceMetadata(0, -1);
		assertThat(unlimitedDataSource.getUsage()).isEqualTo(Float.valueOf(-1F));
	}

	@Override
	public void getValidationQuery() {
		BasicDataSource dataSource = createDataSource();
		dataSource.setValidationQuery("SELECT FROM FOO");
		assertThat(
				new CommonsDbcp2DataSourcePoolMetadata(dataSource).getValidationQuery())
						.isEqualTo("SELECT FROM FOO");
	}

	@Override
	public void getDefaultAutoCommit() {
		BasicDataSource dataSource = createDataSource();
		dataSource.setDefaultAutoCommit(false);
		assertThat(
				new CommonsDbcp2DataSourcePoolMetadata(dataSource).getDefaultAutoCommit())
						.isFalse();
	}

	private CommonsDbcp2DataSourcePoolMetadata createDataSourceMetadata(int minSize,
			int maxSize) {
		BasicDataSource dataSource = createDataSource();
		dataSource.setMinIdle(minSize);
		dataSource.setMaxTotal(maxSize);
		return new CommonsDbcp2DataSourcePoolMetadata(dataSource);
	}

	private BasicDataSource createDataSource() {
		return initializeBuilder().type(BasicDataSource.class).build();
	}

}
