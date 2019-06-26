

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleGenericStringServiceCaller;
import org.springframework.boot.test.mock.mockito.example.SimpleExampleStringGenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Test {@link SpyBean} on a test class field can be used to inject a spy instance when
 * there are multiple candidates and one is primary.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
public class SpyBeanOnTestFieldForMultipleExistingBeansWithOnePrimaryIntegrationTests {

	@SpyBean
	private SimpleExampleStringGenericService spy;

	@Autowired
	private ExampleGenericStringServiceCaller caller;

	@Test
	public void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say two");
		assertThat(Mockito.mockingDetails(this.spy).getMockCreationSettings()
				.getMockName().toString()).isEqualTo("two");
		verify(this.spy).greeting();
	}

	@Configuration
	@Import(ExampleGenericStringServiceCaller.class)
	static class Config {

		@Bean
		public SimpleExampleStringGenericService one() {
			return new SimpleExampleStringGenericService("one");
		}

		@Bean
		@Primary
		public SimpleExampleStringGenericService two() {
			return new SimpleExampleStringGenericService("two");
		}

	}

}
