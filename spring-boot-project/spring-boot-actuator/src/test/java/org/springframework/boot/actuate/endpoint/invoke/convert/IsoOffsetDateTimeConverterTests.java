

package org.springframework.boot.actuate.endpoint.invoke.convert;

import java.time.OffsetDateTime;

import org.junit.Test;

import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link IsoOffsetDateTimeConverter}.
 *
 * @author Phillip Webb
 */
public class IsoOffsetDateTimeConverterTests {

	@Test
	public void convertShouldConvertIsoDate() {
		IsoOffsetDateTimeConverter converter = new IsoOffsetDateTimeConverter();
		OffsetDateTime time = converter.convert("2011-12-03T10:15:30+01:00");
		assertThat(time).isNotNull();
	}

	@Test
	public void registerConverterShouldRegister() {
		DefaultConversionService service = new DefaultConversionService();
		IsoOffsetDateTimeConverter.registerConverter(service);
		OffsetDateTime time = service.convert("2011-12-03T10:15:30+01:00",
				OffsetDateTime.class);
		assertThat(time).isNotNull();
	}

}
