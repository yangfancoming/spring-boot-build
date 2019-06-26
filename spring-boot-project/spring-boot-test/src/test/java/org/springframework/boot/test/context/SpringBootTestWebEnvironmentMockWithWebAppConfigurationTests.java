

package org.springframework.boot.test.context;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} configured with {@link WebEnvironment#MOCK}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@WebAppConfiguration("src/mymain/mywebapp")
public class SpringBootTestWebEnvironmentMockWithWebAppConfigurationTests {

	@Autowired
	private ServletContext servletContext;

	@Test
	public void resourcePath() {
		assertThat(ReflectionTestUtils.getField(this.servletContext, "resourceBasePath"))
				.isEqualTo("src/mymain/mywebapp");
	}

	@Configuration
	@EnableWebMvc
	protected static class Config {

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertyPlaceholder() {
			return new PropertySourcesPlaceholderConfigurer();
		}

	}

}
