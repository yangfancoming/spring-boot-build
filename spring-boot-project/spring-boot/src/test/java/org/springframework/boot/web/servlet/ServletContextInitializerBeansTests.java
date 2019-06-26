

package org.springframework.boot.web.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ServletContextInitializerBeans}.
 *
 * @author Andy Wilkinson
 */
public class ServletContextInitializerBeansTests {

	private ConfigurableApplicationContext context;

	@Test
	public void servletThatImplementsServletContextInitializerIsOnlyRegisteredOnce() {
		load(ServletConfiguration.class);
		ServletContextInitializerBeans initializerBeans = new ServletContextInitializerBeans(
				this.context.getBeanFactory());
		assertThat(initializerBeans.size()).isEqualTo(1);
		assertThat(initializerBeans.iterator()).hasOnlyElementsOfType(TestServlet.class);
	}

	@Test
	public void filterThatImplementsServletContextInitializerIsOnlyRegisteredOnce() {
		load(FilterConfiguration.class);
		ServletContextInitializerBeans initializerBeans = new ServletContextInitializerBeans(
				this.context.getBeanFactory());
		assertThat(initializerBeans.size()).isEqualTo(1);
		assertThat(initializerBeans.iterator()).hasOnlyElementsOfType(TestFilter.class);
	}

	private void load(Class<?> configuration) {
		this.context = new AnnotationConfigApplicationContext(configuration);
	}

	static class ServletConfiguration {

		@Bean
		public TestServlet testServlet() {
			return new TestServlet();
		}

	}

	static class FilterConfiguration {

		@Bean
		public TestFilter testFilter() {
			return new TestFilter();
		}

	}

	static class TestServlet extends HttpServlet implements ServletContextInitializer {

		@Override
		public void onStartup(ServletContext servletContext) {

		}

	}

	static class TestFilter implements Filter, ServletContextInitializer {

		@Override
		public void onStartup(ServletContext servletContext) {

		}

		@Override
		public void init(FilterConfig filterConfig) {

		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) {

		}

		@Override
		public void destroy() {

		}

	}

}
