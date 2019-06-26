

package org.springframework.boot.test.json;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.core.ResolvableType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectContent}.
 *
 * @author Phillip Webb
 */
public class ObjectContentTests {

	private static final ExampleObject OBJECT = new ExampleObject();

	private static final ResolvableType TYPE = ResolvableType
			.forClass(ExampleObject.class);

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenObjectIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Object must not be null");
		new ObjectContent<ExampleObject>(TYPE, null);
	}

	@Test
	public void createWhenTypeIsNullShouldCreateContent() {
		ObjectContent<ExampleObject> content = new ObjectContent<>(null, OBJECT);
		assertThat(content).isNotNull();
	}

	@Test
	public void assertThatShouldReturnObjectContentAssert() {
		ObjectContent<ExampleObject> content = new ObjectContent<>(TYPE, OBJECT);
		assertThat(content.assertThat()).isInstanceOf(ObjectContentAssert.class);
	}

	@Test
	public void getObjectShouldReturnObject() {
		ObjectContent<ExampleObject> content = new ObjectContent<>(TYPE, OBJECT);
		assertThat(content.getObject()).isEqualTo(OBJECT);
	}

	@Test
	public void toStringWhenHasTypeShouldReturnString() {
		ObjectContent<ExampleObject> content = new ObjectContent<>(TYPE, OBJECT);
		assertThat(content.toString())
				.isEqualTo("ObjectContent " + OBJECT + " created from " + TYPE);
	}

	@Test
	public void toStringWhenHasNoTypeShouldReturnString() {
		ObjectContent<ExampleObject> content = new ObjectContent<>(null, OBJECT);
		assertThat(content.toString()).isEqualTo("ObjectContent " + OBJECT);
	}

}
