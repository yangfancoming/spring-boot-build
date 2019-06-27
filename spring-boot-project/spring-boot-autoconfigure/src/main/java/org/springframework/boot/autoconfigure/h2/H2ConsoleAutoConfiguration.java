

package org.springframework.boot.autoconfigure.h2;

import org.h2.server.web.WebServlet;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for H2's web console.
 *
 * @author Andy Wilkinson
 * @author Marten Deinum
 * @author Stephane Nicoll
 * @since 1.3.0
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(WebServlet.class)
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(H2ConsoleProperties.class)
public class H2ConsoleAutoConfiguration {

	private final H2ConsoleProperties properties;

	public H2ConsoleAutoConfiguration(H2ConsoleProperties properties) {
		this.properties = properties;
	}

	@Bean
	public ServletRegistrationBean<WebServlet> h2Console() {
		String path = this.properties.getPath();
		String urlMapping = path.endsWith("/") ? path + "*" : path + "/*";
		ServletRegistrationBean<WebServlet> registration = new ServletRegistrationBean<>(
				new WebServlet(), urlMapping);
		H2ConsoleProperties.Settings settings = this.properties.getSettings();
		if (settings.isTrace()) {
			registration.addInitParameter("trace", "");
		}
		if (settings.isWebAllowOthers()) {
			registration.addInitParameter("webAllowOthers", "");
		}
		return registration;
	}

}
