

package org.springframework.boot.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringValueResolver;

/**
 * A specialization of {@link FormattingConversionService} configured by default with
 * converters and formatters appropriate for most Spring Boot applications.
 * Designed for direct instantiation but also exposes the static
 * {@link #addApplicationConverters} and
 * {@link #addApplicationFormatters(FormatterRegistry)} utility methods for ad-hoc use against registry instance.
 * @since 2.0.0
 */
public class ApplicationConversionService extends FormattingConversionService {

	private static volatile ApplicationConversionService sharedInstance;

	public ApplicationConversionService() {
		this(null);
	}

	public ApplicationConversionService(StringValueResolver embeddedValueResolver) {
		if (embeddedValueResolver != null) {
			setEmbeddedValueResolver(embeddedValueResolver);
		}
		configure(this);
	}

	/**
	 * Return a shared default {@code ApplicationConversionService} instance, lazily building it once needed.
	 * @return the shared {@code ConversionService} instance (never {@code null})
	 */
	public static ConversionService getSharedInstance() {
		ApplicationConversionService sharedInstance = ApplicationConversionService.sharedInstance;
		if (sharedInstance == null) {
			synchronized (ApplicationConversionService.class) {
				sharedInstance = ApplicationConversionService.sharedInstance;
				if (sharedInstance == null) {
					sharedInstance = new ApplicationConversionService();
					ApplicationConversionService.sharedInstance = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

	/**
	 * Configure the given {@link FormatterRegistry} with formatters and converters appropriate for most Spring Boot applications.
	 * @param registry the registry of converters to add to (must also be castable to ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given FormatterRegistry could not be cast to a ConversionService
	 */
	public static void configure(FormatterRegistry registry) {
		DefaultConversionService.addDefaultConverters(registry);
		DefaultFormattingConversionService.addDefaultFormatters(registry);
		addApplicationFormatters(registry);
		addApplicationConverters(registry);
	}

	/**
	 * Add converters useful for most Spring Boot applications.
	 * @param registry the registry of converters to add to (must also be castable to ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 */
	public static void addApplicationConverters(ConverterRegistry registry) {
		addDelimitedStringConverters(registry);
		registry.addConverter(new StringToDurationConverter());
		registry.addConverter(new DurationToStringConverter());
		registry.addConverter(new NumberToDurationConverter());
		registry.addConverter(new DurationToNumberConverter());
		registry.addConverterFactory(new StringToEnumIgnoringCaseConverterFactory());
	}

	/**
	 * Add converters to support delimited strings.
	 * @param registry the registry of converters to add to (must also be castable to ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 */
	public static void addDelimitedStringConverters(ConverterRegistry registry) {
		ConversionService service = (ConversionService) registry;
		registry.addConverter(new ArrayToDelimitedStringConverter(service));
		registry.addConverter(new CollectionToDelimitedStringConverter(service));
		registry.addConverter(new DelimitedStringToArrayConverter(service));
		registry.addConverter(new DelimitedStringToCollectionConverter(service));
	}

	/**
	 * Add formatters useful for most Spring Boot applications.
	 * @param registry the service to register default formatters with
	 */
	public static void addApplicationFormatters(FormatterRegistry registry) {
		registry.addFormatter(new CharArrayFormatter());
		registry.addFormatter(new InetAddressFormatter());
		registry.addFormatter(new IsoOffsetFormatter());
	}
}
