

package org.springframework.boot.web.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WebServerFactoryCustomizerBeanPostProcessor}.
 *
 * @author Phillip Webb
 */
public class WebServerFactoryCustomizerBeanPostProcessorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private WebServerFactoryCustomizerBeanPostProcessor processor = new WebServerFactoryCustomizerBeanPostProcessor();

	@Mock
	private ListableBeanFactory beanFactory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.processor.setBeanFactory(this.beanFactory);
	}

	@Test
	public void setBeanFactoryWhenNotListableShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("WebServerCustomizerBeanPostProcessor can only "
				+ "be used with a ListableBeanFactory");
		this.processor.setBeanFactory(mock(BeanFactory.class));
	}

	@Test
	public void postProcessBeforeShouldReturnBean() {
		addMockBeans(Collections.emptyMap());
		Object bean = new Object();
		Object result = this.processor.postProcessBeforeInitialization(bean, "foo");
		assertThat(result).isSameAs(bean);
	}

	@Test
	public void postProcessAfterShouldReturnBean() {
		addMockBeans(Collections.emptyMap());
		Object bean = new Object();
		Object result = this.processor.postProcessAfterInitialization(bean, "foo");
		assertThat(result).isSameAs(bean);
	}

	@Test
	public void postProcessAfterShouldCallInterfaceCustomizers() {
		Map<String, Object> beans = addInterfaceBeans();
		addMockBeans(beans);
		postProcessBeforeInitialization(WebServerFactory.class);
		assertThat(wasCalled(beans, "one")).isFalse();
		assertThat(wasCalled(beans, "two")).isFalse();
		assertThat(wasCalled(beans, "all")).isTrue();
	}

	@Test
	public void postProcessAfterWhenWebServerFactoryOneShouldCallInterfaceCustomizers() {
		Map<String, Object> beans = addInterfaceBeans();
		addMockBeans(beans);
		postProcessBeforeInitialization(WebServerFactoryOne.class);
		assertThat(wasCalled(beans, "one")).isTrue();
		assertThat(wasCalled(beans, "two")).isFalse();
		assertThat(wasCalled(beans, "all")).isTrue();
	}

	@Test
	public void postProcessAfterWhenWebServerFactoryTwoShouldCallInterfaceCustomizers() {
		Map<String, Object> beans = addInterfaceBeans();
		addMockBeans(beans);
		postProcessBeforeInitialization(WebServerFactoryTwo.class);
		assertThat(wasCalled(beans, "one")).isFalse();
		assertThat(wasCalled(beans, "two")).isTrue();
		assertThat(wasCalled(beans, "all")).isTrue();
	}

	private Map<String, Object> addInterfaceBeans() {
		WebServerFactoryOneCustomizer oneCustomizer = new WebServerFactoryOneCustomizer();
		WebServerFactoryTwoCustomizer twoCustomizer = new WebServerFactoryTwoCustomizer();
		WebServerFactoryAllCustomizer allCustomizer = new WebServerFactoryAllCustomizer();
		Map<String, Object> beans = new LinkedHashMap<>();
		beans.put("one", oneCustomizer);
		beans.put("two", twoCustomizer);
		beans.put("all", allCustomizer);
		return beans;
	}

	@Test
	public void postProcessAfterShouldCallLambdaCustomizers() {
		List<String> called = new ArrayList<>();
		addLambdaBeans(called);
		postProcessBeforeInitialization(WebServerFactory.class);
		assertThat(called).containsExactly("all");
	}

	@Test
	public void postProcessAfterWhenWebServerFactoryOneShouldCallLambdaCustomizers() {
		List<String> called = new ArrayList<>();
		addLambdaBeans(called);
		postProcessBeforeInitialization(WebServerFactoryOne.class);
		assertThat(called).containsExactly("one", "all");
	}

	@Test
	public void postProcessAfterWhenWebServerFactoryTwoShouldCallLambdaCustomizers() {
		List<String> called = new ArrayList<>();
		addLambdaBeans(called);
		postProcessBeforeInitialization(WebServerFactoryTwo.class);
		assertThat(called).containsExactly("two", "all");
	}

	private void addLambdaBeans(List<String> called) {
		WebServerFactoryCustomizer<WebServerFactoryOne> one = (f) -> called.add("one");
		WebServerFactoryCustomizer<WebServerFactoryTwo> two = (f) -> called.add("two");
		WebServerFactoryCustomizer<WebServerFactory> all = (f) -> called.add("all");
		Map<String, Object> beans = new LinkedHashMap<>();
		beans.put("one", one);
		beans.put("two", two);
		beans.put("all", all);
		addMockBeans(beans);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addMockBeans(Map<String, ?> beans) {
		given(this.beanFactory.getBeansOfType(WebServerFactoryCustomizer.class, false,
				false)).willReturn((Map<String, WebServerFactoryCustomizer>) beans);
	}

	private void postProcessBeforeInitialization(Class<?> type) {
		this.processor.postProcessBeforeInitialization(mock(type), "foo");
	}

	private boolean wasCalled(Map<String, ?> beans, String name) {
		return ((MockWebServerFactoryCustomizer<?>) beans.get(name)).wasCalled();
	}

	private interface WebServerFactoryOne extends WebServerFactory {

	}

	private interface WebServerFactoryTwo extends WebServerFactory {

	}

	private static class MockWebServerFactoryCustomizer<T extends WebServerFactory>
			implements WebServerFactoryCustomizer<T> {

		private boolean called;

		@Override
		public void customize(T factory) {
			this.called = true;
		}

		public boolean wasCalled() {
			return this.called;
		}

	}

	private static class WebServerFactoryOneCustomizer
			extends MockWebServerFactoryCustomizer<WebServerFactoryOne> {

	}

	private static class WebServerFactoryTwoCustomizer
			extends MockWebServerFactoryCustomizer<WebServerFactoryTwo> {

	}

	private static class WebServerFactoryAllCustomizer
			extends MockWebServerFactoryCustomizer<WebServerFactory> {

	}

}
