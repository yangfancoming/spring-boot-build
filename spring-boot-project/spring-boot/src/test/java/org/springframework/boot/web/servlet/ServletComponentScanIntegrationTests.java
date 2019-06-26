

package org.springframework.boot.web.servlet;

import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.testcomponents.TestMultipartServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ServletComponentScan}
 *
 * @author Andy Wilkinson
 */
public class ServletComponentScanIntegrationTests {

	private AnnotationConfigServletWebServerApplicationContext context;

	@After
	public void cleanUp() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void componentsAreRegistered() {
		this.context = new AnnotationConfigServletWebServerApplicationContext();
		this.context.register(TestConfiguration.class);
		new ServerPortInfoApplicationContextInitializer().initialize(this.context);
		this.context.refresh();
		String port = this.context.getEnvironment().getProperty("local.server.port");
		String response = new RestTemplate()
				.getForObject("http://localhost:" + port + "/test", String.class);
		assertThat(response).isEqualTo("alpha bravo");
	}

	@Test
	public void multipartConfigIsHonoured() {
		this.context = new AnnotationConfigServletWebServerApplicationContext();
		this.context.register(TestConfiguration.class);
		new ServerPortInfoApplicationContextInitializer().initialize(this.context);
		this.context.refresh();
		@SuppressWarnings("rawtypes")
		Map<String, ServletRegistrationBean> beans = this.context
				.getBeansOfType(ServletRegistrationBean.class);
		ServletRegistrationBean<?> servletRegistrationBean = beans
				.get(TestMultipartServlet.class.getName());
		assertThat(servletRegistrationBean).isNotNull();
		MultipartConfigElement multipartConfig = servletRegistrationBean
				.getMultipartConfig();
		assertThat(multipartConfig).isNotNull();
		assertThat(multipartConfig.getLocation()).isEqualTo("test");
		assertThat(multipartConfig.getMaxRequestSize()).isEqualTo(2048);
		assertThat(multipartConfig.getMaxFileSize()).isEqualTo(1024);
		assertThat(multipartConfig.getFileSizeThreshold()).isEqualTo(512);
	}

	@Configuration
	@ServletComponentScan(basePackages = "org.springframework.boot.web.servlet.testcomponents")
	static class TestConfiguration {

		@Bean
		public TomcatServletWebServerFactory webServerFactory() {
			return new TomcatServletWebServerFactory(0);
		}

	}

}
