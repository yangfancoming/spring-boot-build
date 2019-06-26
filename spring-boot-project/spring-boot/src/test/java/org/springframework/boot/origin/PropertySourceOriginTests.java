

package org.springframework.boot.origin;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * Tests for {@link PropertySourceOrigin}.
 *
 * @author Phillip Webb
 */
public class PropertySourceOriginTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenPropertySourceIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("PropertySource must not be null");
		new PropertySourceOrigin(null, "name");
	}

	@Test
	public void createWhenPropertyNameIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("PropertyName must not be empty");
		new PropertySourceOrigin(mock(PropertySource.class), null);
	}

	@Test
	public void createWhenPropertyNameIsEmptyShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("PropertyName must not be empty");
		new PropertySourceOrigin(mock(PropertySource.class), "");
	}

	@Test
	public void getPropertySourceShouldReturnPropertySource() {
		MapPropertySource propertySource = new MapPropertySource("test", new HashMap<>());
		PropertySourceOrigin origin = new PropertySourceOrigin(propertySource, "foo");
		assertThat(origin.getPropertySource()).isEqualTo(propertySource);
	}

	@Test
	public void getPropertyNameShouldReturnPropertyName() {
		MapPropertySource propertySource = new MapPropertySource("test", new HashMap<>());
		PropertySourceOrigin origin = new PropertySourceOrigin(propertySource, "foo");
		assertThat(origin.getPropertyName()).isEqualTo("foo");
	}

	@Test
	public void toStringShouldShowDetails() {
		MapPropertySource propertySource = new MapPropertySource("test", new HashMap<>());
		PropertySourceOrigin origin = new PropertySourceOrigin(propertySource, "foo");
		assertThat(origin.toString()).isEqualTo("\"foo\" from property source \"test\"");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void getWhenPropertySourceSupportsOriginLookupShouldReturnOrigin() {
		Origin origin = mock(Origin.class);
		PropertySource<?> propertySource = mock(PropertySource.class,
				withSettings().extraInterfaces(OriginLookup.class));
		OriginLookup<String> originCapablePropertySource = (OriginLookup<String>) propertySource;
		given(originCapablePropertySource.getOrigin("foo")).willReturn(origin);
		assertThat(PropertySourceOrigin.get(propertySource, "foo")).isSameAs(origin);
	}

	@Test
	public void getWhenPropertySourceSupportsOriginLookupButNoOriginShouldWrap() {
		PropertySource<?> propertySource = mock(PropertySource.class,
				withSettings().extraInterfaces(OriginLookup.class));
		assertThat(PropertySourceOrigin.get(propertySource, "foo"))
				.isInstanceOf(PropertySourceOrigin.class);
	}

	@Test
	public void getWhenPropertySourceIsNotOriginAwareShouldWrap() {
		MapPropertySource propertySource = new MapPropertySource("test", new HashMap<>());
		PropertySourceOrigin origin = new PropertySourceOrigin(propertySource, "foo");
		assertThat(origin.getPropertySource()).isEqualTo(propertySource);
		assertThat(origin.getPropertyName()).isEqualTo("foo");
	}

}
