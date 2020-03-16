

package org.springframework.boot.autoconfigure.context;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConfigurationPropertiesAutoConfiguration}.
 */
public class ConfigurationPropertiesAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@Test
	public void processAnnotatedBean() {
		load(new Class[] { AutoConfig.class, SampleBean.class }, "foo.name:test1");
		assertThat(context.getBean(SampleBean.class).getName()).isEqualTo("test1");
	}

	@Test
	public void processAnnotatedBeanNoAutoConfig() {
		load(new Class[] { SampleBean.class }, "foo.name:test");
		assertThat(context.getBean(SampleBean.class).getName()).isEqualTo("default");
	}

	private void load(Class<?>[] configs, String... environment) {
		context = new AnnotationConfigApplicationContext();
		context.register(configs);
		TestPropertyValues.of(environment).applyTo(context);
		context.refresh();
	}

	@Configuration
	@ImportAutoConfiguration(ConfigurationPropertiesAutoConfiguration.class)
	static class AutoConfig {}


	@Component
	@ConfigurationProperties("foo")
	static class SampleBean {
		private String name = "default";
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	@After
	public void tearDown() {
		if (context != null) {
			context.close();
		}
	}

}
