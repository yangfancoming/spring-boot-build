

package org.springframework.boot.devtools.restart;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link OnInitializedRestarterCondition}.
 *
 * @author Phillip Webb
 */
public class OnInitializedRestarterConditionTests {

	private static Object wait = new Object();

	@Before
	@After
	public void cleanup() {
		Restarter.clearInstance();
	}

	@Test
	public void noInstance() {
		Restarter.clearInstance();
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
				Config.class);
		assertThat(context.containsBean("bean")).isFalse();
		context.close();
	}

	@Test
	public void noInitialization() {
		Restarter.initialize(new String[0], false, RestartInitializer.NONE);
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
				Config.class);
		assertThat(context.containsBean("bean")).isFalse();
		context.close();
	}

	@Test
	public void initialized() throws Exception {
		Thread thread = new Thread(TestInitialized::main);
		thread.start();
		synchronized (wait) {
			wait.wait();
		}
	}

	public static class TestInitialized {

		public static void main(String... args) {
			RestartInitializer initializer = mock(RestartInitializer.class);
			given(initializer.getInitialUrls(any(Thread.class))).willReturn(new URL[0]);
			Restarter.initialize(new String[0], false, initializer);
			ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
					Config.class);
			assertThat(context.containsBean("bean")).isTrue();
			context.close();
			synchronized (wait) {
				wait.notify();
			}
		}

	}

	@Configuration
	public static class Config {

		@Bean
		@ConditionalOnInitializedRestarter
		public String bean() {
			return "bean";
		}

	}

}
