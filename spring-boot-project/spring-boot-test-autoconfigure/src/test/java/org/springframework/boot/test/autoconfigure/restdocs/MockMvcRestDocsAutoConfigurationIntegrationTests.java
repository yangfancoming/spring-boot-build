

package org.springframework.boot.test.autoconfigure.restdocs;

import java.io.File;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Integration tests for {@link RestDocsAutoConfiguration} with Mock MVC.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.example.com", uriPort = 443)
public class MockMvcRestDocsAutoConfigurationIntegrationTests {

	@Before
	public void deleteSnippets() {
		FileSystemUtils.deleteRecursively(new File("target/generated-snippets"));
	}

	@Autowired
	private MockMvc mvc;

	@Test
	public void defaultSnippetsAreWritten() throws Exception {
		this.mvc.perform(get("/")).andDo(document("default-snippets"));
		File defaultSnippetsDir = new File("target/generated-snippets/default-snippets");
		assertThat(defaultSnippetsDir).exists();
		assertThat(new File(defaultSnippetsDir, "curl-request.adoc"))
				.has(contentContaining("'https://api.example.com/'"));
		assertThat(new File(defaultSnippetsDir, "http-request.adoc"))
				.has(contentContaining("api.example.com"));
		assertThat(new File(defaultSnippetsDir, "http-response.adoc")).isFile();
	}

	private Condition<File> contentContaining(String toContain) {
		return new ContentContainingCondition(toContain);
	}

}
