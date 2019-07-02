

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * Integration tests for {@link WebMvcTest} and Spring HATEOAS.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
public class WebMvcTestHateoasIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void plainResponse() throws Exception {
		this.mockMvc.perform(get("/hateoas/plain")).andExpect(header()
				.string(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8"));
	}

	@Test
	public void hateoasResponse() throws Exception {
		this.mockMvc.perform(get("/hateoas/resource")).andExpect(header()
				.string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"));
	}

}
