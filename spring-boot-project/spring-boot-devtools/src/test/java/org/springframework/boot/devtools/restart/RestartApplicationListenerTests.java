

package org.springframework.boot.devtools.restart;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link RestartApplicationListener}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class RestartApplicationListenerTests {

	private static final String ENABLED_PROPERTY = "spring.devtools.restart.enabled";

	private static final String[] ARGS = new String[] { "a", "b", "c" };

	@Before
	@After
	public void cleanup() {
		Restarter.clearInstance();
		System.clearProperty(ENABLED_PROPERTY);
	}

	@Test
	public void isHighestPriority() {
		assertThat(new RestartApplicationListener().getOrder())
				.isEqualTo(Ordered.HIGHEST_PRECEDENCE);
	}

	@Test
	public void initializeWithReady() {
		testInitialize(false);
		assertThat(ReflectionTestUtils.getField(Restarter.getInstance(), "args"))
				.isEqualTo(ARGS);
		assertThat(Restarter.getInstance().isFinished()).isTrue();
		assertThat((List<?>) ReflectionTestUtils.getField(Restarter.getInstance(),
				"rootContexts")).isNotEmpty();
	}

	@Test
	public void initializeWithFail() {
		testInitialize(true);
		assertThat(ReflectionTestUtils.getField(Restarter.getInstance(), "args"))
				.isEqualTo(ARGS);
		assertThat(Restarter.getInstance().isFinished()).isTrue();
		assertThat((List<?>) ReflectionTestUtils.getField(Restarter.getInstance(),
				"rootContexts")).isEmpty();
	}

	@Test
	public void disableWithSystemProperty() {
		System.setProperty(ENABLED_PROPERTY, "false");
		testInitialize(false);
		assertThat(ReflectionTestUtils.getField(Restarter.getInstance(), "enabled"))
				.isEqualTo(false);
	}

	private void testInitialize(boolean failed) {
		Restarter.clearInstance();
		RestartApplicationListener listener = new RestartApplicationListener();
		SpringApplication application = new SpringApplication();
		ConfigurableApplicationContext context = mock(
				ConfigurableApplicationContext.class);
		listener.onApplicationEvent(new ApplicationStartingEvent(application, ARGS));
		assertThat(Restarter.getInstance()).isNotEqualTo(nullValue());
		assertThat(Restarter.getInstance().isFinished()).isFalse();
		listener.onApplicationEvent(
				new ApplicationPreparedEvent(application, ARGS, context));
		if (failed) {
			listener.onApplicationEvent(new ApplicationFailedEvent(application, ARGS,
					context, new RuntimeException()));
		}
		else {
			listener.onApplicationEvent(
					new ApplicationReadyEvent(application, ARGS, context));
		}
	}

}
