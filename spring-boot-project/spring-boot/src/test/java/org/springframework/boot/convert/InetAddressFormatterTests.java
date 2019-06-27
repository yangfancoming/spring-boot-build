

package org.springframework.boot.convert;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InetAddressFormatter}.
 *
 * @author Phillip Webb
 */
@RunWith(Parameterized.class)
public class InetAddressFormatterTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final ConversionService conversionService;

	public InetAddressFormatterTests(String name, ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Test
	public void convertFromInetAddressToStringShouldConvert()
			throws UnknownHostException {
		assumeResolves("example.com", true);
		InetAddress address = InetAddress.getByName("example.com");
		String converted = this.conversionService.convert(address, String.class);
		assertThat(converted).isEqualTo(address.getHostAddress());
	}

	@Test
	public void convertFromStringToInetAddressShouldConvert() {
		assumeResolves("example.com", true);
		InetAddress converted = this.conversionService.convert("example.com",
				InetAddress.class);
		assertThat(converted.toString()).startsWith("example.com");
	}

	@Test
	public void convertFromStringToInetAddressWhenHostDoesNotExistShouldThrowException() {
		String missingDomain = "ireallydontexist.example.com";
		assumeResolves(missingDomain, false);
		this.thrown.expect(ConversionFailedException.class);
		this.conversionService.convert(missingDomain, InetAddress.class);
	}

	private void assumeResolves(String host, boolean expectedToResolve) {
		boolean resolved = isResolvable(host);
		if (resolved != expectedToResolve) {
			throw new AssumptionViolatedException(
					"Host " + host + " resolved " + resolved);
		}
	}

	private boolean isResolvable(String host) {
		try {
			InetAddress.getByName(host);
			return true;
		}
		catch (UnknownHostException ex) {
			return false;
		}
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> conversionServices() {
		return new ConversionServiceParameters(new InetAddressFormatter());
	}

}
