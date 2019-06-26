

package org.springframework.boot.autoconfigure.couchbase;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OnBootstrapHostsCondition}.
 *
 * @author Stephane Nicoll
 */
public class OnBootstrapHostsConditionTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void bootstrapHostsNotDefined() {
		load(TestConfig.class);
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	public void bootstrapHostsDefinedAsCommaSeparated() {
		load(TestConfig.class, "spring.couchbase.bootstrap-hosts=value1");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void bootstrapHostsDefinedAsList() {
		load(TestConfig.class, "spring.couchbase.bootstrap-hosts[0]=value1");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void bootstrapHostsDefinedAsCommaSeparatedRelaxed() {
		load(TestConfig.class, "spring.couchbase.bootstrapHosts=value1");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void bootstrapHostsDefinedAsListRelaxed() {
		load(TestConfig.class, "spring.couchbase.bootstrapHosts[0]=value1");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	private void load(Class<?> config, String... environment) {
		this.context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(environment).applyTo(this.context);
		this.context.register(config);
		this.context.refresh();
	}

	@Configuration
	@Conditional(OnBootstrapHostsCondition.class)
	protected static class TestConfig {

		@Bean
		public String foo() {
			return "foo";
		}

	}

}
