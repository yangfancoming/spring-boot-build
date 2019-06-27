

package org.springframework.boot.autoconfigure.security.servlet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Integration test to ensure {@link SecurityFilterAutoConfiguration} doesn't cause early
 * initialization.
 *
 * @author Phillip Webb
 */
public class SecurityFilterAutoConfigurationEarlyInitializationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testSecurityFilterDoesNotCauseEarlyInitialization() {
		try (AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext()) {
			TestPropertyValues.of("server.port:0").applyTo(context);
			context.register(Config.class);
			context.refresh();
			int port = context.getWebServer().getPort();
			String password = this.outputCapture.toString()
					.split("Using generated security password: ")[1].split("\n")[0]
							.trim();
			new TestRestTemplate("user", password)
					.getForEntity("http://localhost:" + port, Object.class);
			// If early initialization occurred a ConverterNotFoundException is thrown

		}
	}

	@Configuration
	@Import({ DeserializerBean.class, JacksonModuleBean.class, ExampleController.class,
			ConverterBean.class })
	@ImportAutoConfiguration({ WebMvcAutoConfiguration.class,
			JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
			DispatcherServletAutoConfiguration.class, SecurityAutoConfiguration.class,
			UserDetailsServiceAutoConfiguration.class,
			SecurityFilterAutoConfiguration.class,
			PropertyPlaceholderAutoConfiguration.class })
	static class Config {

		@Bean
		public TomcatServletWebServerFactory webServerFactory() {
			TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			factory.setPort(0);
			return factory;
		}

	}

	public static class SourceType {

		public String foo;

	}

	public static class DestinationType {

		public String bar;

	}

	@Component
	public static class JacksonModuleBean extends SimpleModule {

		private static final long serialVersionUID = 1L;

		public JacksonModuleBean(DeserializerBean myDeser) {
			addDeserializer(SourceType.class, myDeser);
		}

	}

	@Component
	public static class DeserializerBean extends StdDeserializer<SourceType> {

		@Autowired
		ConversionService conversionService;

		public DeserializerBean() {
			super(SourceType.class);
		}

		@Override
		public SourceType deserialize(JsonParser p, DeserializationContext ctxt) {
			return new SourceType();
		}

	}

	@RestController
	public static class ExampleController {

		@Autowired
		private ConversionService conversionService;

		@RequestMapping("/")
		public void convert() {
			this.conversionService.convert(new SourceType(), DestinationType.class);
		}

	}

	@Component
	public static class ConverterBean implements Converter<SourceType, DestinationType> {

		@Override
		public DestinationType convert(SourceType source) {
			return new DestinationType();
		}

	}

}
