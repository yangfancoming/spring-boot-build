

package org.springframework.boot.test.json;

import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.Test;

import org.springframework.core.ResolvableType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JsonbTester}.
 *
 * @author Eddú Meléndez
 */
public class JsonbTesterTests extends AbstractJsonMarshalTesterTests {

	@Test
	public void initFieldsWhenTestIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("TestInstance must not be null");
		JsonbTester.initFields(null, JsonbBuilder.create());
	}

	@Test
	public void initFieldsWhenMarshallerIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Marshaller must not be null");
		JsonbTester.initFields(new InitFieldsTestClass(), (Jsonb) null);
	}

	@Test
	public void initFieldsShouldSetNullFields() {
		InitFieldsTestClass test = new InitFieldsTestClass();
		assertThat(test.test).isNull();
		assertThat(test.base).isNull();
		JsonbTester.initFields(test, JsonbBuilder.create());
		assertThat(test.test).isNotNull();
		assertThat(test.base).isNotNull();
		assertThat(test.test.getType().resolve()).isEqualTo(List.class);
		assertThat(test.test.getType().resolveGeneric()).isEqualTo(ExampleObject.class);
	}

	@Override
	protected AbstractJsonMarshalTester<Object> createTester(Class<?> resourceLoadClass,
			ResolvableType type) {
		return new JsonbTester<>(resourceLoadClass, type, JsonbBuilder.create());
	}

	abstract static class InitFieldsBaseClass {

		public JsonbTester<ExampleObject> base;

		public JsonbTester<ExampleObject> baseSet = new JsonbTester<>(
				InitFieldsBaseClass.class, ResolvableType.forClass(ExampleObject.class),
				JsonbBuilder.create());

	}

	static class InitFieldsTestClass extends InitFieldsBaseClass {

		public JsonbTester<List<ExampleObject>> test;

		public JsonbTester<ExampleObject> testSet = new JsonbTester<>(
				InitFieldsBaseClass.class, ResolvableType.forClass(ExampleObject.class),
				JsonbBuilder.create());

	}

}
