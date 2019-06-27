

package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ExtendedWhitespaceThrowableProxyConverter}.
 *
 * @author Phillip Webb
 * @author Chanwit Kaewkasi
 */
public class ExtendedWhitespaceThrowableProxyConverterTests {

	private final ExtendedWhitespaceThrowableProxyConverter converter = new ExtendedWhitespaceThrowableProxyConverter();

	private final LoggingEvent event = new LoggingEvent();

	@Test
	public void noStackTrace() {
		String s = this.converter.convert(this.event);
		assertThat(s).isEmpty();
	}

	@Test
	public void withStackTrace() {
		this.event.setThrowableProxy(new ThrowableProxy(new RuntimeException()));
		String s = this.converter.convert(this.event);
		assertThat(s).startsWith(System.lineSeparator()).endsWith(System.lineSeparator());
	}

}
