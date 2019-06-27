

package org.springframework.boot.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JsonComponentModule}.
 *
 * @author Phillip Webb
 * @author Vladimir Tsanev
 */
public class JsonComponentModuleTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void closeContext() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void moduleShouldRegisterSerializers() throws Exception {
		load(OnlySerializer.class);
		JsonComponentModule module = this.context.getBean(JsonComponentModule.class);
		assertSerialize(module);
	}

	@Test
	public void moduleShouldRegisterDeserializers() throws Exception {
		load(OnlyDeserializer.class);
		JsonComponentModule module = this.context.getBean(JsonComponentModule.class);
		assertDeserialize(module);
	}

	@Test
	public void moduleShouldRegisterInnerClasses() throws Exception {
		load(NameAndAgeJsonComponent.class);
		JsonComponentModule module = this.context.getBean(JsonComponentModule.class);
		assertSerialize(module);
		assertDeserialize(module);
	}

	@Test
	public void moduleShouldAllowInnerAbstractClasses() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				JsonComponentModule.class, ComponentWithInnerAbstractClass.class);
		JsonComponentModule module = context.getBean(JsonComponentModule.class);
		assertSerialize(module);
		context.close();
	}

	private void load(Class<?>... configs) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(configs);
		context.register(JsonComponentModule.class);
		context.refresh();
		this.context = context;
	}

	private void assertSerialize(Module module) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);
		String json = mapper.writeValueAsString(new NameAndAge("spring", 100));
		assertThat(json).isEqualToIgnoringWhitespace("{\"name\":\"spring\",\"age\":100}");
	}

	private void assertDeserialize(Module module) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);
		NameAndAge nameAndAge = mapper.readValue("{\"name\":\"spring\",\"age\":100}",
				NameAndAge.class);
		assertThat(nameAndAge.getName()).isEqualTo("spring");
		assertThat(nameAndAge.getAge()).isEqualTo(100);
	}

	@JsonComponent
	static class OnlySerializer extends NameAndAgeJsonComponent.Serializer {

	}

	@JsonComponent
	static class OnlyDeserializer extends NameAndAgeJsonComponent.Deserializer {

	}

	@JsonComponent
	static class ComponentWithInnerAbstractClass {

		private abstract static class AbstractSerializer
				extends NameAndAgeJsonComponent.Serializer {

		}

		static class ConcreteSerializer extends AbstractSerializer {

		}

	}

}
