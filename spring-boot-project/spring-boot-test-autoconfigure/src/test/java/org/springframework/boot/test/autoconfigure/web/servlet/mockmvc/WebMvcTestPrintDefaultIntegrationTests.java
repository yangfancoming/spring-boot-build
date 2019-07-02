

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link WebMvcTest} default print output.
 *
 * @author Phillip Webb
 */
@RunWith(WebMvcTestPrintDefaultRunner.class)
@WebMvcTest
@AutoConfigureMockMvc(secure = false)
public class WebMvcTestPrintDefaultIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldNotPrint() throws Exception {
		this.mvc.perform(get("/one")).andExpect(content().string("one"))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldPrint() throws Exception {
		this.mvc.perform(get("/one")).andExpect(content().string("none"))
				.andExpect(status().isOk());
	}

}
