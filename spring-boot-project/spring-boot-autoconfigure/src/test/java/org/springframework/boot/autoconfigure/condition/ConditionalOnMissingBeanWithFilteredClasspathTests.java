

package org.springframework.boot.autoconfigure.condition;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests {@link ConditionalOnMissingBean} with filtered classpath.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("spring-context-support-*.jar")
public class ConditionalOnMissingBeanWithFilteredClasspathTests {

	private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@After
	public void closeContext() {
		this.context.close();
	}

	@Test
	public void testNameOnMissingBeanTypeWithMissingImport() {
		this.context.register(OnBeanTypeConfiguration.class);
		this.context.refresh();
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Configuration
	static class OnBeanTypeConfiguration {

		@Bean
		@ConditionalOnMissingBean(type = "org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBeanWithFilteredClasspathTests.TestCacheManager")
		public String foo() {
			return "foo";
		}

	}

	static class TestCacheManager extends CaffeineCacheManager {

	}

}
