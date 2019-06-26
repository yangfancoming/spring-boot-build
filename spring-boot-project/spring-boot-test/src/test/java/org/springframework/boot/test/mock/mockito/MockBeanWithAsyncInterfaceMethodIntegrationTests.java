

package org.springframework.boot.test.mock.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for a mock bean where the mocked interface has an async method.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
public class MockBeanWithAsyncInterfaceMethodIntegrationTests {

	@MockBean
	private Transformer transformer;

	@Autowired
	private MyService service;

	@Test
	public void mockedMethodsAreNotAsync() {
		given(this.transformer.transform("foo")).willReturn("bar");
		assertThat(this.service.transform("foo")).isEqualTo("bar");
	}

	private interface Transformer {

		@Async
		String transform(String input);

	}

	private static class MyService {

		private final Transformer transformer;

		MyService(Transformer transformer) {
			this.transformer = transformer;
		}

		public String transform(String input) {
			return this.transformer.transform(input);
		}

	}

	@Configuration
	@EnableAsync
	static class MyConfiguration {

		@Bean
		public MyService myService(Transformer transformer) {
			return new MyService(transformer);
		}

	}

}
