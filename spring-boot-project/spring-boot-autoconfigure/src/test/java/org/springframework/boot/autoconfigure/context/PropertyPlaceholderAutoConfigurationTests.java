

package org.springframework.boot.autoconfigure.context;

import org.junit.After;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PropertyPlaceholderAutoConfiguration}.
 */
public class PropertyPlaceholderAutoConfigurationTests {

	private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@Test
	public void propertyPlaceholders() {
		context.register(PropertyPlaceholderAutoConfiguration.class,PlaceholderConfig.class);
		TestPropertyValues.of("foo:two").applyTo(context);
		context.refresh();
		assertThat(context.getBean(PlaceholderConfig.class).getFoo()).isEqualTo("two");
	}

	@Test
	public void propertyPlaceholdersOverride() {
		context.register(PropertyPlaceholderAutoConfiguration.class,PlaceholderConfig.class, PlaceholdersOverride.class);
		TestPropertyValues.of("foo:two").applyTo(context);
		context.refresh();
		assertThat(context.getBean(PlaceholderConfig.class).getFoo()).isEqualTo("spam");
	}

	@Configuration
	static class PlaceholderConfig {
		@Value("${foo:bar}")
		private String foo;
		public String getFoo() {
			return foo;
		}
	}

	@Configuration
	static class PlaceholdersOverride {
		@Bean
		public static PropertySourcesPlaceholderConfigurer morePlaceholders() {
			PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setProperties(StringUtils.splitArrayElementsIntoProperties(new String[] { "foo=spam" }, "="));
			configurer.setLocalOverride(true);
			configurer.setOrder(0);
			return configurer;
		}
	}

	@After
	public void close() {
		if (context != null) {
			context.close();
		}
	}
}
