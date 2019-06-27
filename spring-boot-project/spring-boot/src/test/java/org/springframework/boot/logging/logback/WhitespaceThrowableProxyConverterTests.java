

package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WhitespaceThrowableProxyConverter}.
 *
 * @author Phillip Webb
 * @author Chanwit Kaewkasi
 */
public class WhitespaceThrowableProxyConverterTests {

	private final WhitespaceThrowableProxyConverter converter = new WhitespaceThrowableProxyConverter();

	private final LoggingEvent event = new LoggingEvent();

	@Test
	public void noStackTrace() {
		String s = this.converter.convert(this.event);
		assertThat(s).isEqualTo("");
	}

	@Test
	public void withStackTrace() {
		this.event.setThrowableProxy(new ThrowableProxy(new RuntimeException()));
		String s = this.converter.convert(this.event);
		assertThat(s).startsWith(System.lineSeparator()).endsWith(System.lineSeparator());
	}

}
