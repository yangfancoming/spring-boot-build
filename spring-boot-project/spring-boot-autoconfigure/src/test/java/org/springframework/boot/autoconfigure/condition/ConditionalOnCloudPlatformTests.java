

package org.springframework.boot.autoconfigure.condition;

import org.junit.Test;

import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnCloudPlatform}.
 */
public class ConditionalOnCloudPlatformTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void outcomeWhenCloudfoundryPlatformNotPresentShouldNotMatch() {
		this.contextRunner.withUserConfiguration(CloudFoundryPlatformConfig.class)
				.run((context) -> assertThat(context).doesNotHaveBean("foo"));
	}

	@Test
	public void outcomeWhenCloudfoundryPlatformPresentShouldMatch() {
		this.contextRunner.withUserConfiguration(CloudFoundryPlatformConfig.class)
				.withPropertyValues("VCAP_APPLICATION:---")
				.run((context) -> assertThat(context).hasBean("foo"));
	}

	@Test
	public void outcomeWhenCloudfoundryPlatformPresentAndMethodTargetShouldMatch() {
		this.contextRunner.withUserConfiguration(CloudFoundryPlatformOnMethodConfig.class)
				.withPropertyValues("VCAP_APPLICATION:---")
				.run((context) -> assertThat(context).hasBean("foo"));
	}

	@Configuration
	@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
	static class CloudFoundryPlatformConfig {

		@Bean
		public String foo() {
			return "foo";
		}

	}

	@Configuration
	static class CloudFoundryPlatformOnMethodConfig {

		@Bean
		@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
		public String foo() {
			return "foo";
		}

	}

}
