

package org.springframework.boot.autoconfigure.http;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HttpMessageConvertersAutoConfiguration} without Jackson on the
 * classpath.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("jackson-*.jar")
public class HttpMessageConvertersAutoConfigurationWithoutJacksonTests {

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void autoConfigurationWorksWithSpringHateoasButWithoutJackson() {
		this.context.register(HttpMessageConvertersAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeansOfType(HttpMessageConverters.class)).hasSize(1);
	}

}
