

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link WebMvcTest} to validate converters are discovered.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExampleController2.class, secure = false)
public class WebMvcTestConverterIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldFindConverter() throws Exception {
		String id = UUID.randomUUID().toString();
		this.mvc.perform(get("/two/" + id)).andExpect(content().string(id + "two"))
				.andExpect(status().isOk());
	}

}
