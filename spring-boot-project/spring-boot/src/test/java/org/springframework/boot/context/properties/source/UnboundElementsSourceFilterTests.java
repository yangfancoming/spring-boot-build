

package org.springframework.boot.context.properties.source;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link UnboundElementsSourceFilter}.
 *
 * @author Madhura Bhave
 */
public class UnboundElementsSourceFilterTests {

	private UnboundElementsSourceFilter filter;

	private ConfigurationPropertySource source;

	@Before
	public void setUp() {
		this.filter = new UnboundElementsSourceFilter();
		this.source = mock(ConfigurationPropertySource.class);
	}

	@Test
	public void filterWhenSourceIsSystemPropertiesPropertySourceShouldReturnFalse() {
		MockPropertySource propertySource = new MockPropertySource(
				StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
		given(this.source.getUnderlyingSource()).willReturn(propertySource);
		assertThat(this.filter.apply(this.source)).isFalse();
	}

	@Test
	public void filterWhenSourceIsSystemEnvironmentPropertySourceShouldReturnFalse() {
		MockPropertySource propertySource = new MockPropertySource(
				StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
		given(this.source.getUnderlyingSource()).willReturn(propertySource);
		assertThat(this.filter.apply(this.source)).isFalse();
	}

	@Test
	public void filterWhenSourceIsNotSystemShouldReturnTrue() {
		MockPropertySource propertySource = new MockPropertySource("test");
		given(this.source.getUnderlyingSource()).willReturn(propertySource);
		assertThat(this.filter.apply(this.source)).isTrue();
	}

}
