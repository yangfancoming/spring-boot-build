

package org.springframework.boot.web.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.GenericFilterBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;

/**
 * Tests for {@link DelegatingFilterProxyRegistrationBean}.
 *
 * @author Phillip Webb
 */
public class DelegatingFilterProxyRegistrationBeanTests
		extends AbstractFilterRegistrationBeanTests {

	private static ThreadLocal<Boolean> mockFilterInitialized = new ThreadLocal<>();

	private GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(
			new MockServletContext());

	@Test
	public void targetBeanNameMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("TargetBeanName must not be null or empty");
		new DelegatingFilterProxyRegistrationBean(null);
	}

	@Test
	public void targetBeanNameMustNotBeEmpty() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("TargetBeanName must not be null or empty");
		new DelegatingFilterProxyRegistrationBean("");
	}

	@Test
	public void nameDefaultsToTargetBeanName() {
		assertThat(new DelegatingFilterProxyRegistrationBean("myFilter")
				.getOrDeduceName(null)).isEqualTo("myFilter");
	}

	@Test
	public void getFilterUsesDelegatingFilterProxy() {
		DelegatingFilterProxyRegistrationBean registrationBean = createFilterRegistrationBean();
		Filter filter = registrationBean.getFilter();
		assertThat(filter).isInstanceOf(DelegatingFilterProxy.class);
		assertThat(ReflectionTestUtils.getField(filter, "webApplicationContext"))
				.isEqualTo(this.applicationContext);
		assertThat(ReflectionTestUtils.getField(filter, "targetBeanName"))
				.isEqualTo("mockFilter");
	}

	@Test
	public void initShouldNotCauseEarlyInitialization() throws Exception {
		this.applicationContext.registerBeanDefinition("mockFilter",
				new RootBeanDefinition(MockFilter.class));
		DelegatingFilterProxyRegistrationBean registrationBean = createFilterRegistrationBean();
		Filter filter = registrationBean.getFilter();
		filter.init(new MockFilterConfig());
		assertThat(mockFilterInitialized.get()).isNull();
		filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(),
				new MockFilterChain());
		assertThat(mockFilterInitialized.get()).isTrue();
	}

	@Test
	public void createServletRegistrationBeanMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("ServletRegistrationBeans must not be null");
		new DelegatingFilterProxyRegistrationBean("mockFilter",
				(ServletRegistrationBean[]) null);
	}

	@Override
	protected DelegatingFilterProxyRegistrationBean createFilterRegistrationBean(
			ServletRegistrationBean<?>... servletRegistrationBeans) {
		DelegatingFilterProxyRegistrationBean bean = new DelegatingFilterProxyRegistrationBean(
				"mockFilter", servletRegistrationBeans);
		bean.setApplicationContext(this.applicationContext);
		return bean;
	}

	@Override
	protected Filter getExpectedFilter() {
		return isA(DelegatingFilterProxy.class);
	}

	static class MockFilter extends GenericFilterBean {

		MockFilter() {
			mockFilterInitialized.set(true);
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) {
		}

	}

}
