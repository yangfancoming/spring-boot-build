

package org.springframework.boot.autoconfigure.h2;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link H2ConsoleAutoConfiguration}
 *
 * @author Andy Wilkinson
 * @author Marten Deinum
 * @author Stephane Nicoll
 */
public class H2ConsoleAutoConfigurationTests {

	private AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setupContext() {
		this.context.setServletContext(new MockServletContext());
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void consoleIsDisabledByDefault() {
		this.context.register(H2ConsoleAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).isEmpty();
	}

	@Test
	public void propertyCanEnableConsole() {
		this.context.register(H2ConsoleAutoConfiguration.class);
		TestPropertyValues.of("spring.h2.console.enabled:true").applyTo(this.context);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).hasSize(1);
		ServletRegistrationBean<?> registrationBean = this.context
				.getBean(ServletRegistrationBean.class);
		assertThat(registrationBean.getUrlMappings()).contains("/h2-console/*");
		assertThat(registrationBean.getInitParameters()).doesNotContainKey("trace");
		assertThat(registrationBean.getInitParameters())
				.doesNotContainKey("webAllowOthers");
	}

	@Test
	public void customPathMustBeginWithASlash() {
		this.thrown.expect(BeanCreationException.class);
		this.thrown.expectMessage("Failed to bind properties under 'spring.h2.console'");
		this.context.register(H2ConsoleAutoConfiguration.class);
		TestPropertyValues
				.of("spring.h2.console.enabled:true", "spring.h2.console.path:custom")
				.applyTo(this.context);
		this.context.refresh();
	}

	@Test
	public void customPathWithTrailingSlash() {
		this.context.register(H2ConsoleAutoConfiguration.class);
		TestPropertyValues
				.of("spring.h2.console.enabled:true", "spring.h2.console.path:/custom/")
				.applyTo(this.context);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).hasSize(1);
		ServletRegistrationBean<?> servletRegistrationBean = this.context
				.getBean(ServletRegistrationBean.class);
		assertThat(servletRegistrationBean.getUrlMappings()).contains("/custom/*");
	}

	@Test
	public void customPath() {
		this.context.register(H2ConsoleAutoConfiguration.class);
		TestPropertyValues
				.of("spring.h2.console.enabled:true", "spring.h2.console.path:/custom")
				.applyTo(this.context);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).hasSize(1);
		ServletRegistrationBean<?> servletRegistrationBean = this.context
				.getBean(ServletRegistrationBean.class);
		assertThat(servletRegistrationBean.getUrlMappings()).contains("/custom/*");
	}

	@Test
	public void customInitParameters() {
		this.context.register(H2ConsoleAutoConfiguration.class);
		TestPropertyValues
				.of("spring.h2.console.enabled:true",
						"spring.h2.console.settings.trace=true",
						"spring.h2.console.settings.webAllowOthers=true")
				.applyTo(this.context);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).hasSize(1);
		ServletRegistrationBean<?> registrationBean = this.context
				.getBean(ServletRegistrationBean.class);
		assertThat(registrationBean.getUrlMappings()).contains("/h2-console/*");
		assertThat(registrationBean.getInitParameters()).containsEntry("trace", "");
		assertThat(registrationBean.getInitParameters()).containsEntry("webAllowOthers",
				"");
	}

}
