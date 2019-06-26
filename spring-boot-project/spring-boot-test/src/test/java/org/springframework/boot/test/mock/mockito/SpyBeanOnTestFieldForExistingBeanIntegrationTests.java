

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleService;
import org.springframework.boot.test.mock.mockito.example.ExampleServiceCaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Test {@link SpyBean} on a test class field can be used to replace existing beans.
 *
 * @author Phillip Webb
 * @see SpyBeanOnTestFieldForExistingBeanCacheIntegrationTests
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpyBeanOnTestFieldForExistingBeanConfig.class)
public class SpyBeanOnTestFieldForExistingBeanIntegrationTests {

	@SpyBean
	private ExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	public void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say simple");
		verify(this.caller.getService()).greeting();
	}

}
