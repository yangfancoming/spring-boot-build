

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.example.ExampleServiceCaller;
import org.springframework.boot.test.mock.mockito.example.SimpleExampleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

/**
 * Integration tests for using {@link SpyBean} with {@link DirtiesContext} and
 * {@link ClassMode#BEFORE_EACH_TEST_METHOD}.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class SpyBeanWithDirtiesContextClassModeBeforeMethodIntegrationTests {

	@SpyBean
	private SimpleExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	public void testSpying() throws Exception {
		this.caller.sayGreeting();
		verify(this.exampleService).greeting();
	}

	@Configuration
	@Import(ExampleServiceCaller.class)
	static class Config {

	}

}
