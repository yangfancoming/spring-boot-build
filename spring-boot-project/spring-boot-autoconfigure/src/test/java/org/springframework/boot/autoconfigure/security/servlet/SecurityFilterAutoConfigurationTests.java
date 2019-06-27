

package org.springframework.boot.autoconfigure.security.servlet;

import org.junit.Test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfigurationTests.WebSecurity;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfigurationEarlyInitializationTests.ConverterBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfigurationEarlyInitializationTests.DeserializerBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfigurationEarlyInitializationTests.ExampleController;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfigurationEarlyInitializationTests.JacksonModuleBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Tests for {@link SecurityFilterAutoConfiguration}.
 *
 * @author Andy Wilkinson
 */
public class SecurityFilterAutoConfigurationTests {

	@Test
	public void filterAutoConfigurationWorksWithoutSecurityAutoConfiguration() {
		try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
			context.setServletContext(new MockServletContext());
			context.register(Config.class);
			context.refresh();
		}
	}

	@Configuration
	@Import({ DeserializerBean.class, JacksonModuleBean.class, ExampleController.class,
			ConverterBean.class })
	@ImportAutoConfiguration({ WebMvcAutoConfiguration.class,
			JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
			DispatcherServletAutoConfiguration.class, WebSecurity.class,
			SecurityFilterAutoConfiguration.class,
			PropertyPlaceholderAutoConfiguration.class })
	static class Config {

	}

}
