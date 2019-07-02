

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for Test {@link DispatcherServlet} customizations.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@TestPropertySource(properties = { "spring.mvc.throw-exception-if-no-handler-found=true",
		"spring.mvc.static-path-pattern=/static/**" })
public class WebMvcTestCustomDispatcherServletIntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void dispatcherServletIsCustomized() throws Exception {
		this.mvc.perform(get("/does-not-exist")).andExpect(status().isBadRequest())
				.andExpect(content().string("Invalid request: /does-not-exist"));
	}

}
