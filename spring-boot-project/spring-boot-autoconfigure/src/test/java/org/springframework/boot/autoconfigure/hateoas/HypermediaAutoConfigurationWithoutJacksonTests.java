

package org.springframework.boot.autoconfigure.hateoas;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Tests for {@link HypermediaAutoConfiguration} when Jackson is not on the classpath.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("jackson-*.jar")
public class HypermediaAutoConfigurationWithoutJacksonTests {

	private AnnotationConfigWebApplicationContext context;

	@Test
	public void jacksonRelatedConfigurationBacksOff() {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.register(BaseConfig.class);
		this.context.setServletContext(new MockServletContext());
		this.context.refresh();
	}

	@ImportAutoConfiguration({ HttpMessageConvertersAutoConfiguration.class,
			WebMvcAutoConfiguration.class, HypermediaAutoConfiguration.class })
	static class BaseConfig {

	}

}
