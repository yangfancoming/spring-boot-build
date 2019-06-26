

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockingDetails;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.example.SimpleExampleStringGenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link SpyBean} on a test class field can be used to inject a spy instance when
 * there are multiple candidates and one is chosen using the name attribute.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
public class SpyBeanWithNameOnTestFieldForMultipleExistingBeansTests {

	@SpyBean(name = "two")
	private SimpleExampleStringGenericService spy;

	@Test
	public void testSpying() {
		MockingDetails mockingDetails = Mockito.mockingDetails(this.spy);
		assertThat(mockingDetails.isSpy()).isTrue();
		assertThat(mockingDetails.getMockCreationSettings().getMockName().toString())
				.isEqualTo("two");
	}

	@Configuration
	static class Config {

		@Bean
		public SimpleExampleStringGenericService one() {
			return new SimpleExampleStringGenericService("one");
		}

		@Bean
		public SimpleExampleStringGenericService two() {
			return new SimpleExampleStringGenericService("two");
		}

	}

}
