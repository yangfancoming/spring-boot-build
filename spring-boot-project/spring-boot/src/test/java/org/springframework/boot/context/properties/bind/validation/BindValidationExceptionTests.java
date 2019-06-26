

package org.springframework.boot.context.properties.bind.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BindValidationException}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class BindValidationExceptionTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenValidationErrorsIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ValidationErrors must not be null");
		new BindValidationException(null);
	}

	@Test
	public void getValidationErrorsShouldReturnValidationErrors() {
		ValidationErrors errors = mock(ValidationErrors.class);
		BindValidationException exception = new BindValidationException(errors);
		assertThat(exception.getValidationErrors()).isEqualTo(errors);
	}

}
