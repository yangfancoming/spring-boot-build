

package org.springframework.boot.context.properties;

import java.util.Collections;

import org.junit.Test;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FilteredPropertySources}.
 *
 * @author Andy Wilkinson
 */
public class FilteredPropertySourcesTests {

	@Test
	public void getReturnsNullForFilteredSource() {
		MutablePropertySources delegate = new MutablePropertySources();
		delegate.addFirst(new MapPropertySource("foo", Collections.emptyMap()));
		assertThat(new FilteredPropertySources(delegate, "foo").get("foo")).isNull();
	}

	@Test
	public void getReturnsSourceThatIsNotFiltered() {
		MutablePropertySources delegate = new MutablePropertySources();
		delegate.addFirst(new MapPropertySource("foo", Collections.emptyMap()));
		MapPropertySource barSource = new MapPropertySource("bar",
				Collections.emptyMap());
		delegate.addFirst(barSource);
		assertThat(new FilteredPropertySources(delegate, "foo").get("bar"))
				.isEqualTo(barSource);
	}

	@Test
	public void containsReturnsFalseForFilteredSource() {
		MutablePropertySources delegate = new MutablePropertySources();
		delegate.addFirst(new MapPropertySource("foo", Collections.emptyMap()));
		assertThat(new FilteredPropertySources(delegate, "foo").contains("foo"))
				.isFalse();
	}

	@Test
	public void containsReturnsTrueForSourceThatIsNotFiltered() {
		MutablePropertySources delegate = new MutablePropertySources();
		delegate.addFirst(new MapPropertySource("foo", Collections.emptyMap()));
		MapPropertySource barSource = new MapPropertySource("bar",
				Collections.emptyMap());
		delegate.addFirst(barSource);
		assertThat(new FilteredPropertySources(delegate, "foo").contains("bar")).isTrue();
	}

	@Test
	public void iteratorOmitsSourceThatIsFiltered() {
		MutablePropertySources delegate = new MutablePropertySources();
		MapPropertySource barSource = new MapPropertySource("bar",
				Collections.emptyMap());
		delegate.addFirst(barSource);
		delegate.addFirst(new MapPropertySource("foo", Collections.emptyMap()));
		MapPropertySource bazSource = new MapPropertySource("baz",
				Collections.emptyMap());
		delegate.addFirst(bazSource);
		assertThat(new FilteredPropertySources(delegate, "foo").iterator())
				.containsExactly(bazSource, barSource);
	}

}
