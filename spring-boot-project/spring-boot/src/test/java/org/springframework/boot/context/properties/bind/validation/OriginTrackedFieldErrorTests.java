

package org.springframework.boot.context.properties.bind.validation;

import org.junit.Test;

import org.springframework.boot.origin.MockOrigin;
import org.springframework.boot.origin.Origin;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OriginTrackedFieldError}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class OriginTrackedFieldErrorTests {

	private static final FieldError FIELD_ERROR = new FieldError("foo", "bar", "faf");

	private static final Origin ORIGIN = MockOrigin.of("afile");

	@Test
	public void ofWhenFieldErrorIsNullShouldReturnNull() {
		assertThat(OriginTrackedFieldError.of(null, ORIGIN)).isNull();
	}

	@Test
	public void ofWhenOriginIsNullShouldReturnFieldErrorWithoutOrigin() {
		assertThat(OriginTrackedFieldError.of(FIELD_ERROR, null)).isSameAs(FIELD_ERROR);
	}

	@Test
	public void ofShouldReturnOriginCapableFieldError() {
		FieldError fieldError = OriginTrackedFieldError.of(FIELD_ERROR, ORIGIN);
		assertThat(fieldError.getObjectName()).isEqualTo("foo");
		assertThat(fieldError.getField()).isEqualTo("bar");
		assertThat(Origin.from(fieldError)).isEqualTo(ORIGIN);
	}

	@Test
	public void toStringShouldAddOrigin() {
		assertThat(OriginTrackedFieldError.of(FIELD_ERROR, ORIGIN).toString()).isEqualTo(
				"Field error in object 'foo' on field 'bar': rejected value [null]"
						+ "; codes []; arguments []; default message [faf]; origin afile");
	}

}
