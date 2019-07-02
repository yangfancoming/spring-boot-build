

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * Tests for {@link WebMvcTest} servlet filter registration.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class WebMvcTestServletFilterIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldApplyFilter() throws Exception {
		this.mvc.perform(get("/one")).andExpect(header().string("x-test", "abc"));
	}

}
