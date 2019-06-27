

package org.springframework.boot;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link SpringBootExceptionHandler}.
 *
 * @author Henri Tremblay
 * @author Andy Wilkinson
 */
public class SpringBootExceptionHandlerTests {

	private final UncaughtExceptionHandler parent = mock(UncaughtExceptionHandler.class);

	private final SpringBootExceptionHandler handler = new SpringBootExceptionHandler(
			this.parent);

	@Test
	public void uncaughtExceptionDoesNotForwardLoggedErrorToParent() {
		Thread thread = Thread.currentThread();
		Exception ex = new Exception();
		this.handler.registerLoggedException(ex);
		this.handler.uncaughtException(thread, ex);
		verifyZeroInteractions(this.parent);
	}

	@Test
	public void uncaughtExceptionForwardsLogConfigurationErrorToParent() {
		Thread thread = Thread.currentThread();
		Exception ex = new Exception(
				"[stuff] Logback configuration error detected [stuff]");
		this.handler.registerLoggedException(ex);
		this.handler.uncaughtException(thread, ex);
		verify(this.parent).uncaughtException(thread, ex);
	}

	@Test
	public void uncaughtExceptionForwardsWrappedLogConfigurationErrorToParent() {
		Thread thread = Thread.currentThread();
		Exception ex = new InvocationTargetException(new Exception(
				"[stuff] Logback configuration error detected [stuff]", new Exception()));
		this.handler.registerLoggedException(ex);
		this.handler.uncaughtException(thread, ex);
		verify(this.parent).uncaughtException(thread, ex);
	}

}
