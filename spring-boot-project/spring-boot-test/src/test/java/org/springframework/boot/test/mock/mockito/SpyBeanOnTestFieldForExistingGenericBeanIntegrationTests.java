

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleGenericService;
import org.springframework.boot.test.mock.mockito.example.ExampleGenericServiceCaller;
import org.springframework.boot.test.mock.mockito.example.SimpleExampleIntegerGenericService;
import org.springframework.boot.test.mock.mockito.example.SimpleExampleStringGenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
public class SpyBeanOnTestFieldForExistingGenericBeanIntegrationTests {

	// gh-7625

	@SpyBean
	private ExampleGenericService<String> exampleService;

	@Autowired
	private ExampleGenericServiceCaller caller;

	@Test
	public void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say 123 simple");
		verify(this.exampleService).greeting();
	}

	@Configuration
	@Import({ ExampleGenericServiceCaller.class,
			SimpleExampleIntegerGenericService.class })
	static class SpyBeanOnTestFieldForExistingBeanConfig {

		@Bean
		public ExampleGenericService<String> simpleExampleStringGenericService() {
			// In order to trigger issue we need a method signature that returns the
			// generic type not the actual implementation class
			return new SimpleExampleStringGenericService();
		}

	}

}
