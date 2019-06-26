

package org.springframework.boot.web.servlet;

import javax.servlet.Filter;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.mock.MockFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link Filter} registration.
 *
 * @author Andy Wilkinson
 */
public class FilterRegistrationIntegrationTests {

	private AnnotationConfigServletWebServerApplicationContext context;

	@After
	public void cleanUp() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void normalFiltersAreRegistered() {
		load(FilterConfiguration.class);
		assertThat(this.context.getServletContext().getFilterRegistrations()).hasSize(1);
	}

	@Test
	public void scopedTargetFiltersAreNotRegistered() {
		load(ScopedTargetFilterConfiguration.class);
		assertThat(this.context.getServletContext().getFilterRegistrations()).isEmpty();
	}

	private void load(Class<?> configuration) {
		this.context = new AnnotationConfigServletWebServerApplicationContext(
				ContainerConfiguration.class, configuration);
	}

	@Configuration
	static class ContainerConfiguration {

		@Bean
		public TomcatServletWebServerFactory webServerFactory() {
			return new TomcatServletWebServerFactory(0);
		}

	}

	@Configuration
	static class ScopedTargetFilterConfiguration {

		@Bean(name = "scopedTarget.myFilter")
		public Filter myFilter() {
			return new MockFilter();
		}

	}

	@Configuration
	static class FilterConfiguration {

		@Bean
		public Filter myFilter() {
			return new MockFilter();
		}

	}

}
