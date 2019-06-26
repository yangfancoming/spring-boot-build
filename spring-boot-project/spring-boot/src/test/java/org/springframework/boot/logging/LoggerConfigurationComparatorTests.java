

package org.springframework.boot.logging;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LoggerConfigurationComparator}.
 *
 * @author Ben Hale
 */
public class LoggerConfigurationComparatorTests {

	private final LoggerConfigurationComparator comparator = new LoggerConfigurationComparator(
			"ROOT");

	@Test
	public void rootLoggerFirst() {
		LoggerConfiguration first = new LoggerConfiguration("ROOT", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isLessThan(0);
	}

	@Test
	public void rootLoggerSecond() {
		LoggerConfiguration first = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("ROOT", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isGreaterThan(0);
	}

	@Test
	public void rootLoggerFirstEmpty() {
		LoggerConfiguration first = new LoggerConfiguration("ROOT", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isLessThan(0);
	}

	@Test
	public void rootLoggerSecondEmpty() {
		LoggerConfiguration first = new LoggerConfiguration("", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("ROOT", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isGreaterThan(0);
	}

	@Test
	public void lexicalFirst() {
		LoggerConfiguration first = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("bravo", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isLessThan(0);
	}

	@Test
	public void lexicalSecond() {
		LoggerConfiguration first = new LoggerConfiguration("bravo", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isGreaterThan(0);
	}

	@Test
	public void lexicalEqual() {
		LoggerConfiguration first = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		LoggerConfiguration second = new LoggerConfiguration("alpha", null, LogLevel.OFF);
		assertThat(this.comparator.compare(first, second)).isEqualTo(0);
	}

}
