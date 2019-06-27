

package org.springframework.boot.autoconfigure.web.servlet;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.web.servlet.DispatcherServlet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DispatcherServletRegistrationBean}.
 *
 * @author Phillip Webb
 */
public class DispatcherServletRegistrationBeanTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenPathIsNullThrowsException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Path must not be null");
		new DispatcherServletRegistrationBean(new DispatcherServlet(), null);
	}

	@Test
	public void getPathReturnsPath() {
		DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(
				new DispatcherServlet(), "/test");
		assertThat(bean.getPath()).isEqualTo("/test");
	}

	@Test
	public void getUrlMappingsReturnsSinglePathMappedPattern() {
		DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(
				new DispatcherServlet(), "/test");
		assertThat(bean.getUrlMappings()).containsOnly("/test/*");
	}

	@Test
	public void setUrlMappingsCannotBeCalled() {
		DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(
				new DispatcherServlet(), "/test");
		this.thrown.expect(UnsupportedOperationException.class);
		bean.setUrlMappings(Collections.emptyList());
	}

	@Test
	public void addUrlMappingsCannotBeCalled() {
		DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(
				new DispatcherServlet(), "/test");
		this.thrown.expect(UnsupportedOperationException.class);
		bean.addUrlMappings("/test");
	}

}
