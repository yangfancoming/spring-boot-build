

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for using {@link SpringBootTest} with {@link TestPropertySource}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = {
		"boot-test-inlined=foo", "b=boot-test-inlined", "c=boot-test-inlined" })
@TestPropertySource(properties = { "property-source-inlined=bar",
		"a=property-source-inlined",
		"c=property-source-inlined" }, locations = "classpath:/test-property-source-annotation.properties")
public class SpringBootTestWithTestPropertySourceTests {

	@Autowired
	private Config config;

	@Test
	public void propertyFromSpringBootTestProperties() {
		assertThat(this.config.bootTestInlined).isEqualTo("foo");
	}

	@Test
	public void propertyFromTestPropertySourceProperties() {
		assertThat(this.config.propertySourceInlined).isEqualTo("bar");
	}

	@Test
	public void propertyFromTestPropertySourceLocations() {
		assertThat(this.config.propertySourceLocation).isEqualTo("baz");
	}

	@Test
	public void propertyFromPropertySourcePropertiesOverridesPropertyFromPropertySourceLocations() {
		assertThat(this.config.propertySourceInlinedOverridesPropertySourceLocation)
				.isEqualTo("property-source-inlined");
	}

	@Test
	public void propertyFromBootTestPropertiesOverridesPropertyFromPropertySourceLocations() {
		assertThat(this.config.bootTestInlinedOverridesPropertySourceLocation)
				.isEqualTo("boot-test-inlined");
	}

	@Test
	public void propertyFromPropertySourcePropertiesOverridesPropertyFromBootTestProperties() {
		assertThat(this.config.propertySourceInlinedOverridesBootTestInlined)
				.isEqualTo("property-source-inlined");
	}

	@Configuration
	static class Config {

		@Value("${boot-test-inlined}")
		private String bootTestInlined;

		@Value("${property-source-inlined}")
		private String propertySourceInlined;

		@Value("${property-source-location}")
		private String propertySourceLocation;

		@Value("${a}")
		private String propertySourceInlinedOverridesPropertySourceLocation;

		@Value("${b}")
		private String bootTestInlinedOverridesPropertySourceLocation;

		@Value("${c}")
		private String propertySourceInlinedOverridesBootTestInlined;

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertyPlaceholder() {
			return new PropertySourcesPlaceholderConfigurer();
		}

	}

}