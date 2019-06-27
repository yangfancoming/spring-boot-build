

package org.springframework.boot.actuate.endpoint.invoke.reflect;

import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameters;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OperationMethod}.
 *
 * @author Phillip Webb
 */
public class OperationMethodTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Method exampleMethod = ReflectionUtils.findMethod(getClass(), "example",
			String.class);

	@Test
	public void createWhenMethodIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Method must not be null");
		new OperationMethod(null, OperationType.READ);
	}

	@Test
	public void createWhenOperationTypeIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("OperationType must not be null");
		new OperationMethod(this.exampleMethod, null);
	}

	@Test
	public void getMethodShouldReturnMethod() {
		OperationMethod operationMethod = new OperationMethod(this.exampleMethod,
				OperationType.READ);
		assertThat(operationMethod.getMethod()).isEqualTo(this.exampleMethod);
	}

	@Test
	public void getOperationTypeShouldReturnOperationType() {
		OperationMethod operationMethod = new OperationMethod(this.exampleMethod,
				OperationType.READ);
		assertThat(operationMethod.getOperationType()).isEqualTo(OperationType.READ);
	}

	@Test
	public void getParametersShouldReturnParameters() {
		OperationMethod operationMethod = new OperationMethod(this.exampleMethod,
				OperationType.READ);
		OperationParameters parameters = operationMethod.getParameters();
		assertThat(parameters.getParameterCount()).isEqualTo(1);
		assertThat(parameters.iterator().next().getName()).isEqualTo("name");
	}

	String example(String name) {
		return name;
	}

}
