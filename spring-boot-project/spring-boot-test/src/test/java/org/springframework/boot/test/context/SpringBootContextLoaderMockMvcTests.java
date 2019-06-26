

package org.springframework.boot.test.context;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link WebAppConfiguration} integration.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@ContextConfiguration(loader = SpringBootContextLoader.class)
@WebAppConfiguration
public class SpringBootContextLoaderMockMvcTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ServletContext servletContext;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void testMockHttpEndpoint() throws Exception {
		this.mvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string("Hello World"));
	}

	@Test
	public void validateWebApplicationContextIsSet() {
		assertThat(this.context).isSameAs(
				WebApplicationContextUtils.getWebApplicationContext(this.servletContext));
	}

	@Configuration
	@EnableWebMvc
	@RestController
	protected static class Config {

		@RequestMapping("/")
		public String home() {
			return "Hello World";
		}

	}

}
