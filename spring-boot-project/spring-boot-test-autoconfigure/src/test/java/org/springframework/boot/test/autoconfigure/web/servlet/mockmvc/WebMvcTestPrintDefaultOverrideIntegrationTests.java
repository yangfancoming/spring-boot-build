

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link WebMvcTest} when a specific controller is defined.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@TestPropertySource(properties = "spring.test.mockmvc.print=NONE")
public class WebMvcTestPrintDefaultOverrideIntegrationTests {

	@Rule
	public OutputCapture output = new OutputCapture();

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldFindController1() throws Exception {
		this.mvc.perform(get("/one")).andExpect(content().string("one"))
				.andExpect(status().isOk());
		assertThat(this.output.toString()).doesNotContain("Request URI = /one");
	}

}
