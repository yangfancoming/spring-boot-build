

package org.springframework.boot.test.web.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link TestRestTemplateContextCustomizer}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class TestRestTemplateContextCustomizerIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test() {
		assertThat(this.restTemplate.getForObject("/", String.class)).contains("hello");
	}

	@Configuration
	@Import({ TestServlet.class, NoTestRestTemplateBeanChecker.class })
	static class TestConfig {

		@Bean
		public TomcatServletWebServerFactory webServerFactory() {
			return new TomcatServletWebServerFactory(0);
		}

	}

	static class TestServlet extends GenericServlet {

		@Override
		public void service(ServletRequest request, ServletResponse response)
				throws ServletException, IOException {
			try (PrintWriter writer = response.getWriter()) {
				writer.println("hello");
			}
		}

	}

}
