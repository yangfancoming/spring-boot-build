

package org.springframework.boot.actuate.autoconfigure.endpoint.condition;

import org.junit.Test;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.EndpointExtension;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnEnabledEndpoint}.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
public class ConditionalOnEnabledEndpointTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void outcomeWhenEndpointEnabledPropertyIsTrueShouldMatch() {
		this.contextRunner.withPropertyValues("management.endpoint.foo.enabled=true")
				.withUserConfiguration(
						FooEndpointEnabledByDefaultFalseConfiguration.class)
				.run((context) -> assertThat(context).hasBean("foo"));
	}

	@Test
	public void outcomeWhenEndpointEnabledPropertyIsFalseShouldNotMatch() {
		this.contextRunner.withPropertyValues("management.endpoint.foo.enabled=false")
				.withUserConfiguration(FooEndpointEnabledByDefaultTrueConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean("foo"));
	}

	@Test
	public void outcomeWhenNoEndpointPropertyAndUserDefinedDefaultIsTrueShouldMatch() {
		this.contextRunner
				.withPropertyValues("management.endpoints.enabled-by-default=true")
				.withUserConfiguration(
						FooEndpointEnabledByDefaultFalseConfiguration.class)
				.run((context) -> assertThat(context).hasBean("foo"));
	}

	@Test
	public void outcomeWhenNoEndpointPropertyAndUserDefinedDefaultIsFalseShouldNotMatch() {
		this.contextRunner
				.withPropertyValues("management.endpoints.enabled-by-default=false")
				.withUserConfiguration(FooEndpointEnabledByDefaultTrueConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean("foo"));
	}

	@Test
	public void outcomeWhenNoPropertiesAndAnnotationIsEnabledByDefaultShouldMatch() {
		this.contextRunner
				.withUserConfiguration(FooEndpointEnabledByDefaultTrueConfiguration.class)
				.run((context) -> assertThat(context).hasBean("foo"));
	}

	@Test
	public void outcomeWhenNoPropertiesAndAnnotationIsNotEnabledByDefaultShouldNotMatch() {
		this.contextRunner
				.withUserConfiguration(
						FooEndpointEnabledByDefaultFalseConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean("foo"));
	}

	@Test
	public void outcomeWhenNoPropertiesAndExtensionAnnotationIsEnabledByDefaultShouldMatch() {
		this.contextRunner
				.withUserConfiguration(
						FooEndpointAndExtensionEnabledByDefaultTrueConfiguration.class)
				.run((context) -> assertThat(context).hasBean("foo").hasBean("fooExt"));
	}

	@Test
	public void outcomeWhenNoPropertiesAndExtensionAnnotationIsNotEnabledByDefaultShouldNotMatch() {
		this.contextRunner
				.withUserConfiguration(
						FooEndpointAndExtensionEnabledByDefaultFalseConfiguration.class)
				.run((context) -> assertThat(context).doesNotHaveBean("foo")
						.doesNotHaveBean("fooExt"));
	}

	@Endpoint(id = "foo", enableByDefault = true)
	static class FooEndpointEnabledByDefaultTrue {

	}

	@Endpoint(id = "foo", enableByDefault = false)
	static class FooEndpointEnabledByDefaultFalse {

	}

	@EndpointExtension(endpoint = FooEndpointEnabledByDefaultTrue.class, filter = TestFilter.class)
	static class FooEndpointExtensionEnabledByDefaultTrue {

	}

	@EndpointExtension(endpoint = FooEndpointEnabledByDefaultFalse.class, filter = TestFilter.class)
	static class FooEndpointExtensionEnabledByDefaultFalse {

	}

	static class TestFilter implements EndpointFilter<ExposableEndpoint<?>> {

		@Override
		public boolean match(ExposableEndpoint<?> endpoint) {
			return true;
		}

	}

	@Configuration
	static class FooEndpointEnabledByDefaultTrueConfiguration {

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointEnabledByDefaultTrue foo() {
			return new FooEndpointEnabledByDefaultTrue();
		}

	}

	@Configuration
	static class FooEndpointEnabledByDefaultFalseConfiguration {

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointEnabledByDefaultFalse foo() {
			return new FooEndpointEnabledByDefaultFalse();
		}

	}

	@Configuration
	static class FooEndpointAndExtensionEnabledByDefaultTrueConfiguration {

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointEnabledByDefaultTrue foo() {
			return new FooEndpointEnabledByDefaultTrue();
		}

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointExtensionEnabledByDefaultTrue fooExt() {
			return new FooEndpointExtensionEnabledByDefaultTrue();
		}

	}

	@Configuration
	static class FooEndpointAndExtensionEnabledByDefaultFalseConfiguration {

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointEnabledByDefaultFalse foo() {
			return new FooEndpointEnabledByDefaultFalse();
		}

		@Bean
		@ConditionalOnEnabledEndpoint
		public FooEndpointExtensionEnabledByDefaultFalse fooExt() {
			return new FooEndpointExtensionEnabledByDefaultFalse();
		}

	}

}
