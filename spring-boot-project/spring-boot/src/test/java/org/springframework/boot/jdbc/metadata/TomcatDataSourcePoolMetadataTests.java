

package org.springframework.boot.jdbc.metadata;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Before;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TomcatDataSourcePoolMetadata}.
 */
public class TomcatDataSourcePoolMetadataTests 	extends AbstractDataSourcePoolMetadataTests<TomcatDataSourcePoolMetadata> {

	private TomcatDataSourcePoolMetadata dataSourceMetadata;

	@Before
	public void setup() {
		this.dataSourceMetadata = new TomcatDataSourcePoolMetadata(createDataSource(0, 2));
	}

	@Override
	protected TomcatDataSourcePoolMetadata getDataSourceMetadata() {
		return this.dataSourceMetadata;
	}

	@Override
	public void getValidationQuery() {
		DataSource dataSource = createDataSource(0, 4);
		dataSource.setValidationQuery("SELECT FROM FOO");
		assertThat(new TomcatDataSourcePoolMetadata(dataSource).getValidationQuery()).isEqualTo("SELECT FROM FOO");
	}

	@Override
	public void getDefaultAutoCommit() {
		DataSource dataSource = createDataSource(0, 4);
		dataSource.setDefaultAutoCommit(false);
		assertThat(new TomcatDataSourcePoolMetadata(dataSource).getDefaultAutoCommit()).isFalse();
	}

	private DataSource createDataSource(int minSize, int maxSize) {
		DataSource dataSource = initializeBuilder().type(DataSource.class).build();
		dataSource.setMinIdle(minSize);
		dataSource.setMaxActive(maxSize);

		// Avoid warnings
		dataSource.setInitialSize(minSize);
		dataSource.setMaxIdle(maxSize);
		return dataSource;
	}

}
