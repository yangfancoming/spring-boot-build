

package org.springframework.boot.test.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

/**
 * AssertJ based JSON tester backed by Jackson. Usually instantiated via
 * {@link #initFields(Object, ObjectMapper)}, for example: <pre class="code">
 * public class ExampleObjectJsonTests {
 *
 *     private JacksonTester&lt;ExampleObject&gt; json;
 *
 *     &#064;Before
 *     public void setup() {
 *         ObjectMapper objectMapper = new ObjectMapper();
 *         JacksonTester.initFields(this, objectMapper);
 *     }
 *
 *     &#064;Test
 *     public void testWriteJson() throws IOException {
 *         ExampleObject object = //...
 *         assertThat(json.write(object)).isEqualToJson("expected.json");
 *     }
 *
 * }
 * </pre>
 *
 * See {@link AbstractJsonMarshalTester} for more details.
 *
 * @param <T> the type under test
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 1.4.0
 */
public class JacksonTester<T> extends AbstractJsonMarshalTester<T> {

	private final ObjectMapper objectMapper;

	private Class<?> view;

	/**
	 * Create a new {@link JacksonTester} instance.
	 * @param objectMapper the Jackson object mapper
	 */
	protected JacksonTester(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
	}

	/**
	 * Create a new {@link JacksonTester} instance.
	 * @param resourceLoadClass the source class used to load resources
	 * @param type the type under test
	 * @param objectMapper the Jackson object mapper
	 */
	public JacksonTester(Class<?> resourceLoadClass, ResolvableType type,
			ObjectMapper objectMapper) {
		this(resourceLoadClass, type, objectMapper, null);
	}

	public JacksonTester(Class<?> resourceLoadClass, ResolvableType type,
			ObjectMapper objectMapper, Class<?> view) {
		super(resourceLoadClass, type);
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
		this.view = view;
	}

	@Override
	protected T readObject(InputStream inputStream, ResolvableType type)
			throws IOException {
		return getObjectReader(type).readValue(inputStream);
	}

	@Override
	protected T readObject(Reader reader, ResolvableType type) throws IOException {
		return getObjectReader(type).readValue(reader);
	}

	private ObjectReader getObjectReader(ResolvableType type) {
		ObjectReader objectReader = this.objectMapper.readerFor(getType(type));
		if (this.view != null) {
			return objectReader.withView(this.view);
		}
		return objectReader;
	}

	@Override
	protected String writeObject(T value, ResolvableType type) throws IOException {
		return getObjectWriter(type).writeValueAsString(value);
	}

	private ObjectWriter getObjectWriter(ResolvableType type) {
		ObjectWriter objectWriter = this.objectMapper.writerFor(getType(type));
		if (this.view != null) {
			return objectWriter.withView(this.view);
		}
		return objectWriter;
	}

	private JavaType getType(ResolvableType type) {
		return this.objectMapper.constructType(type.getType());
	}

	/**
	 * Utility method to initialize {@link JacksonTester} fields. See {@link JacksonTester
	 * class-level documentation} for example usage.
	 * @param testInstance the test instance
	 * @param objectMapper the object mapper
	 * @see #initFields(Object, ObjectMapper)
	 */
	public static void initFields(Object testInstance, ObjectMapper objectMapper) {
		new JacksonFieldInitializer().initFields(testInstance, objectMapper);
	}

	/**
	 * Utility method to initialize {@link JacksonTester} fields. See {@link JacksonTester
	 * class-level documentation} for example usage.
	 * @param testInstance the test instance
	 * @param objectMapperFactory a factory to create the object mapper
	 * @see #initFields(Object, ObjectMapper)
	 */
	public static void initFields(Object testInstance,
			ObjectFactory<ObjectMapper> objectMapperFactory) {
		new JacksonFieldInitializer().initFields(testInstance, objectMapperFactory);
	}

	/**
	 * Returns a new instance of {@link JacksonTester} with the view that should be used
	 * for json serialization/deserialization.
	 * @param view the view class
	 * @return the new instance
	 */
	public JacksonTester<T> forView(Class<?> view) {
		return new JacksonTester<>(this.getResourceLoadClass(), this.getType(),
				this.objectMapper, view);
	}

	/**
	 * {@link FieldInitializer} for Jackson.
	 */
	private static class JacksonFieldInitializer extends FieldInitializer<ObjectMapper> {

		protected JacksonFieldInitializer() {
			super(JacksonTester.class);
		}

		@Override
		protected AbstractJsonMarshalTester<Object> createTester(
				Class<?> resourceLoadClass, ResolvableType type,
				ObjectMapper marshaller) {
			return new JacksonTester<>(resourceLoadClass, type, marshaller);
		}

	}

}