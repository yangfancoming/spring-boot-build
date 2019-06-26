

package org.springframework.boot.logging;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.logging.LoggingSystem.NoOpLoggingSystem;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LoggingSystem}.
 *
 * @author Andy Wilkinson
 */
public class LoggingSystemTests {

	@After
	public void clearSystemProperty() {
		System.clearProperty(LoggingSystem.SYSTEM_PROPERTY);
	}

	@Test
	public void loggingSystemCanBeDisabled() {
		System.setProperty(LoggingSystem.SYSTEM_PROPERTY, LoggingSystem.NONE);
		LoggingSystem loggingSystem = LoggingSystem.get(getClass().getClassLoader());
		assertThat(loggingSystem).isInstanceOf(NoOpLoggingSystem.class);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getLoggerConfigurationIsUnsupported() {
		new StubLoggingSystem().getLoggerConfiguration("test-logger-name");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listLoggerConfigurationsIsUnsupported() {
		new StubLoggingSystem().getLoggerConfigurations();
	}

	private static final class StubLoggingSystem extends LoggingSystem {

		@Override
		public void beforeInitialize() {
			// Stub implementation
		}

		@Override
		public void setLogLevel(String loggerName, LogLevel level) {
			// Stub implementation
		}

	}

}
