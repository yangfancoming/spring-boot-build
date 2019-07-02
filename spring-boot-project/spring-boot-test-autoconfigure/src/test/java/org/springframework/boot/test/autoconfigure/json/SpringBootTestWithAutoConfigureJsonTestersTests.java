

package org.springframework.boot.test.autoconfigure.json;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.app.ExampleBasicObject;
import org.springframework.boot.test.autoconfigure.json.app.ExampleJsonApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link SpringBootTest} with {@link AutoConfigureJsonTesters}.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureJsonTesters
@ContextConfiguration(classes = ExampleJsonApplication.class)
public class SpringBootTestWithAutoConfigureJsonTestersTests {

	@Autowired
	private BasicJsonTester basicJson;

	@Autowired
	private JacksonTester<ExampleBasicObject> jacksonTester;

	@Autowired
	private GsonTester<ExampleBasicObject> gsonTester;

	@Autowired
	private JsonbTester<ExampleBasicObject> jsonbTester;

	@Test
	public void contextLoads() {
		assertThat(this.basicJson).isNotNull();
		assertThat(this.jacksonTester).isNotNull();
		assertThat(this.jsonbTester).isNotNull();
		assertThat(this.gsonTester).isNotNull();
	}

}
