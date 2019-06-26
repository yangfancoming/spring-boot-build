

package org.springframework.boot.test.context.runner;

import org.junit.Test;

import org.springframework.boot.test.context.assertj.AssertableWebApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebApplicationContextRunner}.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
public class WebApplicationContextRunnerTests extends
		AbstractApplicationContextRunnerTests<WebApplicationContextRunner, ConfigurableWebApplicationContext, AssertableWebApplicationContext> {

	@Test
	public void contextShouldHaveMockServletContext() {
		get().run((context) -> assertThat(context.getServletContext())
				.isInstanceOf(MockServletContext.class));
	}

	@Override
	protected WebApplicationContextRunner get() {
		return new WebApplicationContextRunner();
	}

}
