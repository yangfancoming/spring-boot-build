

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;

import org.springframework.boot.test.mock.mockito.example.ExampleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * Tests for {@link MockReset}.
 *
 * @author Phillip Webb
 */
public class MockResetTests {

	@Test
	public void noneAttachesReset() {
		ExampleService mock = mock(ExampleService.class);
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.NONE);
	}

	@Test
	public void withSettingsOfNoneAttachesReset() {
		ExampleService mock = mock(ExampleService.class,
				MockReset.withSettings(MockReset.NONE));
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.NONE);
	}

	@Test
	public void beforeAttachesReset() {
		ExampleService mock = mock(ExampleService.class, MockReset.before());
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.BEFORE);
	}

	@Test
	public void afterAttachesReset() {
		ExampleService mock = mock(ExampleService.class, MockReset.after());
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.AFTER);
	}

	@Test
	public void withSettingsAttachesReset() {
		ExampleService mock = mock(ExampleService.class,
				MockReset.withSettings(MockReset.BEFORE));
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.BEFORE);
	}

	@Test
	public void apply() {
		ExampleService mock = mock(ExampleService.class,
				MockReset.apply(MockReset.AFTER, withSettings()));
		assertThat(MockReset.get(mock)).isEqualTo(MockReset.AFTER);
	}

}
