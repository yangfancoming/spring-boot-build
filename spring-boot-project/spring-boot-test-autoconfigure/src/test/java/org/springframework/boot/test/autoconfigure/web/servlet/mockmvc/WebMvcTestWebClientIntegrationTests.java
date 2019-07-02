

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebMvcTest} with {@link WebClient}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
public class WebMvcTestWebClientIntegrationTests {

	@Autowired
	private WebClient webClient;

	@Test
	public void shouldAutoConfigureWebClient() throws Exception {
		HtmlPage page = this.webClient.getPage("/html");
		assertThat(page.getBody().getTextContent()).isEqualTo("Hello");
	}

}
