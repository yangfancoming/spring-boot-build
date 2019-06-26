

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleServiceCaller;
import org.springframework.boot.test.mock.mockito.example.SimpleExampleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Test {@link SpyBean} on a test class can be used to inject new spy instances.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@SpyBean(SimpleExampleService.class)
public class SpyBeanOnTestClassForNewBeanIntegrationTests {

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	public void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say simple");
		verify(this.caller.getService()).greeting();
	}

	@Configuration
	@Import(ExampleServiceCaller.class)
	static class Config {

	}

}
