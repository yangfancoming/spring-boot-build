

package org.springframework.boot.web.servlet;

import javax.servlet.Filter;

import org.junit.Test;

import org.springframework.boot.web.servlet.mock.MockFilter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link FilterRegistrationBean}.
 *
 * @author Phillip Webb
 */
public class FilterRegistrationBeanTests extends AbstractFilterRegistrationBeanTests {

	private final MockFilter filter = new MockFilter();

	@Test
	public void setFilter() throws Exception {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter(this.filter);
		bean.onStartup(this.servletContext);
		verify(this.servletContext).addFilter("mockFilter", this.filter);
	}

	@Test
	public void setFilterMustNotBeNull() throws Exception {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Filter must not be null");
		bean.onStartup(this.servletContext);
	}

	@Test
	public void constructFilterMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Filter must not be null");
		new FilterRegistrationBean<>(null);
	}

	@Test
	public void createServletRegistrationBeanMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ServletRegistrationBeans must not be null");
		new FilterRegistrationBean<>(this.filter, (ServletRegistrationBean[]) null);
	}

	@Override
	protected AbstractFilterRegistrationBean<MockFilter> createFilterRegistrationBean(
			ServletRegistrationBean<?>... servletRegistrationBeans) {
		return new FilterRegistrationBean<>(this.filter, servletRegistrationBeans);
	}

	@Override
	protected Filter getExpectedFilter() {
		return eq(this.filter);
	}

}
