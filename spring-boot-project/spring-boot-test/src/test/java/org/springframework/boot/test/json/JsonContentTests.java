

package org.springframework.boot.test.json;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.core.ResolvableType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JsonContent}.
 *
 * @author Phillip Webb
 */
public class JsonContentTests {

	private static final String JSON = "{\"name\":\"spring\", \"age\":100}";

	private static final ResolvableType TYPE = ResolvableType
			.forClass(ExampleObject.class);

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenResourceLoadClassIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ResourceLoadClass must not be null");
		new JsonContent<ExampleObject>(null, TYPE, JSON);
	}

	@Test
	public void createWhenJsonIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("JSON must not be null");
		new JsonContent<ExampleObject>(getClass(), TYPE, null);
	}

	@Test
	public void createWhenTypeIsNullShouldCreateContent() {
		JsonContent<ExampleObject> content = new JsonContent<>(getClass(), null, JSON);
		assertThat(content).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void assertThatShouldReturnJsonContentAssert() {
		JsonContent<ExampleObject> content = new JsonContent<>(getClass(), TYPE, JSON);
		assertThat(content.assertThat()).isInstanceOf(JsonContentAssert.class);
	}

	@Test
	public void getJsonShouldReturnJson() {
		JsonContent<ExampleObject> content = new JsonContent<>(getClass(), TYPE, JSON);
		assertThat(content.getJson()).isEqualTo(JSON);

	}

	@Test
	public void toStringWhenHasTypeShouldReturnString() {
		JsonContent<ExampleObject> content = new JsonContent<>(getClass(), TYPE, JSON);
		assertThat(content.toString())
				.isEqualTo("JsonContent " + JSON + " created from " + TYPE);
	}

	@Test
	public void toStringWhenHasNoTypeShouldReturnString() {
		JsonContent<ExampleObject> content = new JsonContent<>(getClass(), null, JSON);
		assertThat(content.toString()).isEqualTo("JsonContent " + JSON);
	}

}
