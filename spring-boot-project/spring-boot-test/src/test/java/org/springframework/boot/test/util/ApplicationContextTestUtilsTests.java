

package org.springframework.boot.test.util;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ApplicationContextTestUtils}.
 *
 * @author Stephane Nicoll
 */
public class ApplicationContextTestUtilsTests {

	@Test
	public void closeNull() {
		ApplicationContextTestUtils.closeAll(null);
	}

	@Test
	public void closeNonClosableContext() {
		ApplicationContext mock = mock(ApplicationContext.class);
		ApplicationContextTestUtils.closeAll(mock);
	}

	@Test
	public void closeContextAndParent() {
		ConfigurableApplicationContext mock = mock(ConfigurableApplicationContext.class);
		ConfigurableApplicationContext parent = mock(
				ConfigurableApplicationContext.class);
		given(mock.getParent()).willReturn(parent);
		given(parent.getParent()).willReturn(null);
		ApplicationContextTestUtils.closeAll(mock);
		verify(mock).getParent();
		verify(mock).close();
		verify(parent).getParent();
		verify(parent).close();
	}

}
