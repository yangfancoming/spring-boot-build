

package org.springframework.boot.jdbc.metadata;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link CompositeDataSourcePoolMetadataProvider}.
 *
 * @author Stephane Nicoll
 */
public class CompositeDataSourcePoolMetadataProviderTests {

	@Mock
	private DataSourcePoolMetadataProvider firstProvider;

	@Mock
	private DataSourcePoolMetadata first;

	@Mock
	private DataSource firstDataSource;

	@Mock
	private DataSourcePoolMetadataProvider secondProvider;

	@Mock
	private DataSourcePoolMetadata second;

	@Mock
	private DataSource secondDataSource;

	@Mock
	private DataSource unknownDataSource;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		given(this.firstProvider.getDataSourcePoolMetadata(this.firstDataSource))
				.willReturn(this.first);
		given(this.firstProvider.getDataSourcePoolMetadata(this.secondDataSource))
				.willReturn(this.second);
	}

	@Test
	public void createWithProviders() {
		CompositeDataSourcePoolMetadataProvider provider = new CompositeDataSourcePoolMetadataProvider(
				Arrays.asList(this.firstProvider, this.secondProvider));
		assertThat(provider.getDataSourcePoolMetadata(this.firstDataSource))
				.isSameAs(this.first);
		assertThat(provider.getDataSourcePoolMetadata(this.secondDataSource))
				.isSameAs(this.second);
		assertThat(provider.getDataSourcePoolMetadata(this.unknownDataSource)).isNull();
	}

}
