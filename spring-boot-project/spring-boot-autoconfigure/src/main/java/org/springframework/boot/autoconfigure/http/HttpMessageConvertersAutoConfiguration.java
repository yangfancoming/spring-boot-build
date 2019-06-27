

package org.springframework.boot.autoconfigure.http;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link HttpMessageConverter}s.
 *
 * @author Dave Syer
 * @author Christian Dupuis
 * @author Piotr Maj
 * @author Oliver Gierke
 * @author David Liu
 * @author Andy Wilkinson
 * @author Sebastien Deleuze
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@Configuration
@ConditionalOnClass(HttpMessageConverter.class)
@AutoConfigureAfter({ GsonAutoConfiguration.class, JacksonAutoConfiguration.class,
		JsonbAutoConfiguration.class })
@Import({ JacksonHttpMessageConvertersConfiguration.class,
		GsonHttpMessageConvertersConfiguration.class,
		JsonbHttpMessageConvertersConfiguration.class })
public class HttpMessageConvertersAutoConfiguration {

	static final String PREFERRED_MAPPER_PROPERTY = "spring.http.converters.preferred-json-mapper";

	private final List<HttpMessageConverter<?>> converters;

	public HttpMessageConvertersAutoConfiguration(
			ObjectProvider<List<HttpMessageConverter<?>>> convertersProvider) {
		this.converters = convertersProvider.getIfAvailable();
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpMessageConverters messageConverters() {
		return new HttpMessageConverters(
				(this.converters != null) ? this.converters : Collections.emptyList());
	}

	@Configuration
	@ConditionalOnClass(StringHttpMessageConverter.class)
	@EnableConfigurationProperties(HttpEncodingProperties.class)
	protected static class StringHttpMessageConverterConfiguration {

		private final HttpEncodingProperties encodingProperties;

		protected StringHttpMessageConverterConfiguration(
				HttpEncodingProperties encodingProperties) {
			this.encodingProperties = encodingProperties;
		}

		@Bean
		@ConditionalOnMissingBean
		public StringHttpMessageConverter stringHttpMessageConverter() {
			StringHttpMessageConverter converter = new StringHttpMessageConverter(
					this.encodingProperties.getCharset());
			converter.setWriteAcceptCharset(false);
			return converter;
		}

	}

}
