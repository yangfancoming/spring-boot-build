

package org.springframework.boot.logging.log4j2;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ExtendedWhitespaceThrowablePatternConverter}.
 *
 * @author Vladimir Tsanev
 * @author Phillip Webb
 */
public class ExtendedWhitespaceThrowablePatternConverterTests {

	private final ThrowablePatternConverter converter = ExtendedWhitespaceThrowablePatternConverter
			.newInstance(new DefaultConfiguration(), new String[] {});

	@Test
	public void noStackTrace() {
		LogEvent event = Log4jLogEvent.newBuilder().build();
		StringBuilder builder = new StringBuilder();
		this.converter.format(event, builder);
		assertThat(builder.toString()).isEqualTo("");
	}

	@Test
	public void withStackTrace() {
		LogEvent event = Log4jLogEvent.newBuilder().setThrown(new Exception()).build();
		StringBuilder builder = new StringBuilder();
		this.converter.format(event, builder);
		assertThat(builder).startsWith(System.lineSeparator())
				.endsWith(System.lineSeparator());
	}

}
