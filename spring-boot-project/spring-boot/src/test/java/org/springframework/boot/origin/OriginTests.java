

package org.springframework.boot.origin;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Origin}.
 *
 * @author Phillip Webb
 */
public class OriginTests {

	@Test
	public void fromWhenSourceIsNullShouldReturnNull() {
		assertThat(Origin.from(null)).isNull();
	}

	@Test
	public void fromWhenSourceIsRegularObjectShouldReturnNull() {
		Object source = new Object();
		assertThat(Origin.from(source)).isNull();
	}

	@Test
	public void fromWhenSourceIsOriginShouldReturnSource() {
		Origin origin = mock(Origin.class);
		assertThat(Origin.from(origin)).isEqualTo(origin);
	}

	@Test
	public void fromWhenSourceIsOriginProviderShouldReturnProvidedOrigin() {
		Origin origin = mock(Origin.class);
		OriginProvider originProvider = mock(OriginProvider.class);
		given(originProvider.getOrigin()).willReturn(origin);
		assertThat(Origin.from(origin)).isEqualTo(origin);
	}

	@Test
	public void fromWhenSourceIsThrowableShouldUseCause() {
		Origin origin = mock(Origin.class);
		Exception exception = new RuntimeException(new TestException(origin, null));
		assertThat(Origin.from(exception)).isEqualTo(origin);
	}

	@Test
	public void fromWhenSourceIsThrowableAndOriginProviderThatReturnsNullShouldUseCause() {
		Origin origin = mock(Origin.class);
		Exception exception = new TestException(null, new TestException(origin, null));
		assertThat(Origin.from(exception)).isEqualTo(origin);
	}

	private static class TestException extends RuntimeException
			implements OriginProvider {

		private final Origin origin;

		TestException(Origin origin, Throwable cause) {
			super(cause);
			this.origin = origin;
		}

		@Override
		public Origin getOrigin() {
			return this.origin;
		}

	}

}
