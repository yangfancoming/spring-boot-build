

package org.springframework.boot.test.context;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} configured with {@link WebEnvironment#MOCK}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest("value=123")
@DirtiesContext
public class SpringBootTestWebEnvironmentMockTests {

	@Value("${value}")
	private int value = 0;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ServletContext servletContext;

	@Test
	public void annotationAttributesOverridePropertiesFile() {
		assertThat(this.value).isEqualTo(123);
	}

	@Test
	public void validateWebApplicationContextIsSet() {
		WebApplicationContext fromServletContext = WebApplicationContextUtils
				.getWebApplicationContext(this.servletContext);
		assertThat(fromServletContext).isSameAs(this.context);
	}

	@Test
	public void setsRequestContextHolder() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		assertThat(attributes).isNotNull();
	}

	@Test
	public void resourcePath() {
		assertThat(ReflectionTestUtils.getField(this.servletContext, "resourceBasePath"))
				.isEqualTo("src/main/webapp");
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
