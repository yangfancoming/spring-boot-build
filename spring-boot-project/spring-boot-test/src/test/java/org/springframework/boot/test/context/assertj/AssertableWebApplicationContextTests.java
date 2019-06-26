

package org.springframework.boot.test.context.assertj;

import org.junit.Test;

import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link AssertableWebApplicationContext}.
 *
 * @author Phillip Webb
 * @see ApplicationContextAssertProviderTests
 */
public class AssertableWebApplicationContextTests {

	@Test
	public void getShouldReturnProxy() {
		AssertableWebApplicationContext context = AssertableWebApplicationContext
				.get(() -> mock(ConfigurableWebApplicationContext.class));
		assertThat(context).isInstanceOf(ConfigurableWebApplicationContext.class);
	}

}
