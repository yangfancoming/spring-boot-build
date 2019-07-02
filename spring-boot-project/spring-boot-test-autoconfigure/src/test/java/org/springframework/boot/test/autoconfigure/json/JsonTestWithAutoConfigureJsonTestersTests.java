

package org.springframework.boot.test.autoconfigure.json;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.app.ExampleBasicObject;
import org.springframework.boot.test.autoconfigure.json.app.ExampleJsonApplication;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link JsonTest} with {@link AutoConfigureJsonTesters}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@JsonTest
@AutoConfigureJsonTesters(enabled = false)
@ContextConfiguration(classes = ExampleJsonApplication.class)
public class JsonTestWithAutoConfigureJsonTestersTests {

	@Autowired(required = false)
	private BasicJsonTester basicJson;

	@Autowired(required = false)
	private JacksonTester<ExampleBasicObject> jacksonTester;

	@Autowired(required = false)
	private GsonTester<ExampleBasicObject> gsonTester;

	@Autowired(required = false)
	private JsonbTester<ExampleBasicObject> jsonbTester;

	@Test
	public void basicJson() {
		assertThat(this.basicJson).isNull();
	}

	@Test
	public void jackson() {
		assertThat(this.jacksonTester).isNull();
	}

	@Test
	public void gson() {
		assertThat(this.gsonTester).isNull();
	}

	@Test
	public void jsonb() {
		assertThat(this.jsonbTester).isNull();
	}

}
