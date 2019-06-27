

package org.springframework.boot.actuate.endpoint.annotation;

import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DiscoveredOperationMethod}.
 *
 * @author Phillip Webb
 */
public class DiscoveredOperationMethodTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenAnnotationAttributesIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("AnnotationAttributes must not be null");
		Method method = ReflectionUtils.findMethod(getClass(), "example");
		new DiscoveredOperationMethod(method, OperationType.READ, null);
	}

	@Test
	public void getProducesMediaTypesShouldReturnMediaTypes() {
		Method method = ReflectionUtils.findMethod(getClass(), "example");
		AnnotationAttributes annotationAttributes = new AnnotationAttributes();
		String[] produces = new String[] { "application/json" };
		annotationAttributes.put("produces", produces);
		DiscoveredOperationMethod discovered = new DiscoveredOperationMethod(method,
				OperationType.READ, annotationAttributes);
		assertThat(discovered.getProducesMediaTypes())
				.containsExactly("application/json");
	}

	public void example() {
	}

}
