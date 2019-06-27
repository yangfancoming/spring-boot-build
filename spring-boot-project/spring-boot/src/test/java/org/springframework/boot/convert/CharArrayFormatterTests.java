

package org.springframework.boot.convert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.springframework.core.convert.ConversionService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CharArrayFormatter}.
 *
 * @author Phillip Webb
 */
@RunWith(Parameterized.class)
public class CharArrayFormatterTests {

	private final ConversionService conversionService;

	public CharArrayFormatterTests(String name, ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Test
	public void convertFromCharArrayToStringShouldConvert() {
		char[] source = { 'b', 'o', 'o', 't' };
		String converted = this.conversionService.convert(source, String.class);
		assertThat(converted).isEqualTo("boot");
	}

	@Test
	public void convertFromStringToCharArrayShouldConvert() {
		String source = "boot";
		char[] converted = this.conversionService.convert(source, char[].class);
		assertThat(converted).containsExactly('b', 'o', 'o', 't');
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> conversionServices() {
		return new ConversionServiceParameters(new CharArrayFormatter());
	}

}
