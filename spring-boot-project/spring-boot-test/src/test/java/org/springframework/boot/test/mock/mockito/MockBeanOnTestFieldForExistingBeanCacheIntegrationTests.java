

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleService;
import org.springframework.boot.test.mock.mockito.example.ExampleServiceCaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link MockBean} on a test class field can be used to replace existing beans when
 * the context is cached. This test is identical to
 * {@link MockBeanOnTestFieldForExistingBeanIntegrationTests} so one of them should
 * trigger application context caching.
 *
 * @author Phillip Webb
 * @see MockBeanOnTestFieldForExistingBeanIntegrationTests
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MockBeanOnTestFieldForExistingBeanConfig.class)
public class MockBeanOnTestFieldForExistingBeanCacheIntegrationTests {

	@MockBean
	private ExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	public void testMocking() {
		given(this.exampleService.greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

}
