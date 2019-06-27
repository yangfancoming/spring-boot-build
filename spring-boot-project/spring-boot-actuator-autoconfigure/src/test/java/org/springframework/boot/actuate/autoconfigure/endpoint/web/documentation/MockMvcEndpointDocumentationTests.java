

package org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Abstract base class for tests that generate endpoint documentation using Spring REST
 * Docs and {@link MockMvc}.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class MockMvcEndpointDocumentationTests
		extends AbstractEndpointDocumentationTests {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	protected MockMvc mockMvc;

	@Autowired
	private WebApplicationContext applicationContext;

	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext)
				.apply(MockMvcRestDocumentation
						.documentationConfiguration(this.restDocumentation).uris())
				.build();
	}

	protected WebApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

}
