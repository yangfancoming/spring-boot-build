

package org.springframework.boot.actuate.endpoint.invoke.reflect;

import java.lang.reflect.Method;

import org.junit.Test;

import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OperationMethodParameter}.
 *
 * @author Phillip Webb
 */
public class OperationMethodParameterTests {

	private Method method = ReflectionUtils.findMethod(getClass(), "example",
			String.class, String.class);

	@Test
	public void getNameShouldReturnName() {
		OperationMethodParameter parameter = new OperationMethodParameter("name",
				this.method.getParameters()[0]);
		assertThat(parameter.getName()).isEqualTo("name");
	}

	@Test
	public void getTypeShouldReturnType() {
		OperationMethodParameter parameter = new OperationMethodParameter("name",
				this.method.getParameters()[0]);
		assertThat(parameter.getType()).isEqualTo(String.class);
	}

	@Test
	public void isMandatoryWhenNoAnnotationShouldReturnTrue() {
		OperationMethodParameter parameter = new OperationMethodParameter("name",
				this.method.getParameters()[0]);
		assertThat(parameter.isMandatory()).isTrue();
	}

	@Test
	public void isMandatoryWhenNullableAnnotationShouldReturnFalse() {
		OperationMethodParameter parameter = new OperationMethodParameter("name",
				this.method.getParameters()[1]);
		assertThat(parameter.isMandatory()).isFalse();
	}

	void example(String one, @Nullable String two) {

	}

}
