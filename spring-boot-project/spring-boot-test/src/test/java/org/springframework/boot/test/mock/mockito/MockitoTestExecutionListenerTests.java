

package org.springframework.boot.test.mock.mockito;

import java.io.InputStream;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link MockitoTestExecutionListener}.
 *
 * @author Phillip Webb
 */
public class MockitoTestExecutionListenerTests {

	private MockitoTestExecutionListener listener = new MockitoTestExecutionListener();

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private MockitoPostProcessor postProcessor;

	@Captor
	private ArgumentCaptor<Field> fieldCaptor;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		given(this.applicationContext.getBean(MockitoPostProcessor.class))
				.willReturn(this.postProcessor);
	}

	@Test
	public void prepareTestInstanceShouldInitMockitoAnnotations() throws Exception {
		WithMockitoAnnotations instance = new WithMockitoAnnotations();
		this.listener.prepareTestInstance(mockTestContext(instance));
		assertThat(instance.mock).isNotNull();
		assertThat(instance.captor).isNotNull();
	}

	@Test
	public void prepareTestInstanceShouldInjectMockBean() throws Exception {
		WithMockBean instance = new WithMockBean();
		this.listener.prepareTestInstance(mockTestContext(instance));
		verify(this.postProcessor).inject(this.fieldCaptor.capture(), eq(instance),
				any(MockDefinition.class));
		assertThat(this.fieldCaptor.getValue().getName()).isEqualTo("mockBean");
	}

	@Test
	public void beforeTestMethodShouldDoNothingWhenDirtiesContextAttributeIsNotSet()
			throws Exception {
		WithMockBean instance = new WithMockBean();
		this.listener.beforeTestMethod(mockTestContext(instance));
		verifyNoMoreInteractions(this.postProcessor);
	}

	@Test
	public void beforeTestMethodShouldInjectMockBeanWhenDirtiesContextAttributeIsSet()
			throws Exception {
		WithMockBean instance = new WithMockBean();
		TestContext mockTestContext = mockTestContext(instance);
		given(mockTestContext.getAttribute(
				DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE))
						.willReturn(Boolean.TRUE);
		this.listener.beforeTestMethod(mockTestContext);
		verify(this.postProcessor).inject(this.fieldCaptor.capture(), eq(instance),
				(MockDefinition) any());
		assertThat(this.fieldCaptor.getValue().getName()).isEqualTo("mockBean");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TestContext mockTestContext(Object instance) {
		TestContext testContext = mock(TestContext.class);
		given(testContext.getTestInstance()).willReturn(instance);
		given(testContext.getTestClass()).willReturn((Class) instance.getClass());
		given(testContext.getApplicationContext()).willReturn(this.applicationContext);
		return testContext;
	}

	static class WithMockitoAnnotations {

		@Mock
		InputStream mock;

		@Captor
		ArgumentCaptor<InputStream> captor;

	}

	static class WithMockBean {

		@MockBean
		InputStream mockBean;

	}

}
