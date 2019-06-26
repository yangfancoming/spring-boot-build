

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for disabling JMX by default
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class SpringBootTestJmxTests {

	@Value("${spring.jmx.enabled}")
	private boolean jmx;

	@Test
	public void disabledByDefault() {
		assertThat(this.jmx).isFalse();
	}

	@Configuration
	protected static class Config {

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

	}

}
