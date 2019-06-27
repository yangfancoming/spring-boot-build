

package org.springframework.boot.autoconfigure.jackson;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties to configure Jackson.
 *
 * @author Andy Wilkinson
 * @author Marcel Overdijk
 * @author Johannes Edmeier
 * @since 1.2.0
 */
@ConfigurationProperties(prefix = "spring.jackson")
public class JacksonProperties {

	/**
	 * Date format string or a fully-qualified date format class name. For instance,
	 * `yyyy-MM-dd HH:mm:ss`.
	 */
	private String dateFormat;

	/**
	 * Joda date time format string. If not configured, "date-format" is used as a
	 * fallback if it is configured with a format string.
	 */
	private String jodaDateTimeFormat;

	/**
	 * One of the constants on Jackson's PropertyNamingStrategy. Can also be a
	 * fully-qualified class name of a PropertyNamingStrategy subclass.
	 */
	private String propertyNamingStrategy;

	/**
	 * Jackson on/off features that affect the way Java objects are serialized.
	 */
	private Map<SerializationFeature, Boolean> serialization = new EnumMap<>(
			SerializationFeature.class);

	/**
	 * Jackson on/off features that affect the way Java objects are deserialized.
	 */
	private Map<DeserializationFeature, Boolean> deserialization = new EnumMap<>(
			DeserializationFeature.class);

	/**
	 * Jackson general purpose on/off features.
	 */
	private Map<MapperFeature, Boolean> mapper = new EnumMap<>(MapperFeature.class);

	/**
	 * Jackson on/off features for parsers.
	 */
	private Map<JsonParser.Feature, Boolean> parser = new EnumMap<>(
			JsonParser.Feature.class);

	/**
	 * Jackson on/off features for generators.
	 */
	private Map<JsonGenerator.Feature, Boolean> generator = new EnumMap<>(
			JsonGenerator.Feature.class);

	/**
	 * Controls the inclusion of properties during serialization. Configured with one of
	 * the values in Jackson's JsonInclude.Include enumeration.
	 */
	private JsonInclude.Include defaultPropertyInclusion;

	/**
	 * Time zone used when formatting dates. For instance, "America/Los_Angeles" or
	 * "GMT+10".
	 */
	private TimeZone timeZone = null;

	/**
	 * Locale used for formatting.
	 */
	private Locale locale;

	public String getDateFormat() {
		return this.dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getJodaDateTimeFormat() {
		return this.jodaDateTimeFormat;
	}

	public void setJodaDateTimeFormat(String jodaDataTimeFormat) {
		this.jodaDateTimeFormat = jodaDataTimeFormat;
	}

	public String getPropertyNamingStrategy() {
		return this.propertyNamingStrategy;
	}

	public void setPropertyNamingStrategy(String propertyNamingStrategy) {
		this.propertyNamingStrategy = propertyNamingStrategy;
	}

	public Map<SerializationFeature, Boolean> getSerialization() {
		return this.serialization;
	}

	public Map<DeserializationFeature, Boolean> getDeserialization() {
		return this.deserialization;
	}

	public Map<MapperFeature, Boolean> getMapper() {
		return this.mapper;
	}

	public Map<JsonParser.Feature, Boolean> getParser() {
		return this.parser;
	}

	public Map<JsonGenerator.Feature, Boolean> getGenerator() {
		return this.generator;
	}

	public JsonInclude.Include getDefaultPropertyInclusion() {
		return this.defaultPropertyInclusion;
	}

	public void setDefaultPropertyInclusion(
			JsonInclude.Include defaultPropertyInclusion) {
		this.defaultPropertyInclusion = defaultPropertyInclusion;
	}

	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
