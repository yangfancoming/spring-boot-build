

package org.springframework.boot.autoconfigure.web;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnEnabledResourceChain}.
 *
 * @author Stephane Nicoll
 */
public class ConditionalOnEnabledResourceChainTests {

	private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@After
	public void closeContext() {
		this.context.close();
	}

	@Test
	public void disabledByDefault() {
		load();
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	public void disabledExplicitly() {
		load("spring.resources.chain.enabled:false");
		assertThat(this.context.containsBean("foo")).isFalse();
	}

	@Test
	public void enabledViaMainEnabledFlag() {
		load("spring.resources.chain.enabled:true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void enabledViaFixedStrategyFlag() {
		load("spring.resources.chain.strategy.fixed.enabled:true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	@Test
	public void enabledViaContentStrategyFlag() {
		load("spring.resources.chain.strategy.content.enabled:true");
		assertThat(this.context.containsBean("foo")).isTrue();
	}

	private void load(String... environment) {
		this.context.register(Config.class);
		TestPropertyValues.of(environment).applyTo(this.context);
		this.context.refresh();
	}

	@Configuration
	static class Config {

		@Bean
		@ConditionalOnEnabledResourceChain
		public String foo() {
			return "foo";
		}

	}

}
