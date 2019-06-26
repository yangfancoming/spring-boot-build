

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleGenericService;
import org.springframework.boot.test.mock.mockito.example.ExampleGenericServiceCaller;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link MockBean} on a test class field can be used to inject new mock instances.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
public class MockBeanWithGenericsOnTestFieldForNewBeanIntegrationTests {

	@MockBean
	private ExampleGenericService<Integer> exampleIntegerService;

	@MockBean
	private ExampleGenericService<String> exampleStringService;

	@Autowired
	private ExampleGenericServiceCaller caller;

	@Test
	public void testMocking() {
		given(this.exampleIntegerService.greeting()).willReturn(200);
		given(this.exampleStringService.greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say 200 Boot");
	}

	@Configuration
	@Import(ExampleGenericServiceCaller.class)
	static class Config {

	}

}
