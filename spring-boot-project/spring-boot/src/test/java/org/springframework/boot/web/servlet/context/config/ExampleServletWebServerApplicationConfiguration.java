

package org.springframework.boot.web.servlet.context.config;

import javax.servlet.Servlet;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContextTests;
import org.springframework.boot.web.servlet.mock.MockServlet;
import org.springframework.boot.web.servlet.server.MockServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Example {@code @Configuration} for use with
 * {@link AnnotationConfigServletWebServerApplicationContextTests}.
 *
 * @author Phillip Webb
 */
@Configuration
public class ExampleServletWebServerApplicationConfiguration {

	@Bean
	public MockServletWebServerFactory webServerFactory() {
		return new MockServletWebServerFactory();
	}

	@Bean
	public Servlet servlet() {
		return new MockServlet();
	}

}
