

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleService;
import org.springframework.boot.test.mock.mockito.example.ExampleServiceCaller;
import org.springframework.boot.test.mock.mockito.example.FailingExampleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link MockBean} on a configuration class can be used to replace existing beans.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
public class MockBeanOnConfigurationClassForExistingBeanIntegrationTests {

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	public void testMocking() {
		given(this.caller.getService().greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

	@Configuration
	@MockBean(ExampleService.class)
	@Import({ ExampleServiceCaller.class, FailingExampleService.class })
	static class Config {

	}

}
