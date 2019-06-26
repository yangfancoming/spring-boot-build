

package org.springframework.boot.jdbc.metadata;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HikariDataSourcePoolMetadata}.
 *
 * @author Stephane Nicoll
 */
public class HikariDataSourcePoolMetadataTests
		extends AbstractDataSourcePoolMetadataTests<HikariDataSourcePoolMetadata> {

	private HikariDataSourcePoolMetadata dataSourceMetadata;

	@Before
	public void setup() {
		this.dataSourceMetadata = new HikariDataSourcePoolMetadata(
				createDataSource(0, 2));
	}

	@Override
	protected HikariDataSourcePoolMetadata getDataSourceMetadata() {
		return this.dataSourceMetadata;
	}

	@Override
	public void getValidationQuery() {
		HikariDataSource dataSource = createDataSource(0, 4);
		dataSource.setConnectionTestQuery("SELECT FROM FOO");
		assertThat(new HikariDataSourcePoolMetadata(dataSource).getValidationQuery())
				.isEqualTo("SELECT FROM FOO");
	}

	@Override
	public void getDefaultAutoCommit() {
		HikariDataSource dataSource = createDataSource(0, 4);
		dataSource.setAutoCommit(false);
		assertThat(new HikariDataSourcePoolMetadata(dataSource).getDefaultAutoCommit())
				.isFalse();
	}

	private HikariDataSource createDataSource(int minSize, int maxSize) {
		HikariDataSource dataSource = initializeBuilder().type(HikariDataSource.class)
				.build();
		dataSource.setMinimumIdle(minSize);
		dataSource.setMaximumPoolSize(maxSize);
		return dataSource;
	}

}
