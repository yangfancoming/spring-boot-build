

package org.springframework.boot.actuate.autoconfigure.redis;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.actuate.redis.RedisReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedisHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions({ "reactor-core*.jar", "lettuce-core*.jar" })
public class RedisHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(RedisAutoConfiguration.class,
					RedisHealthIndicatorAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(RedisHealthIndicator.class)
						.doesNotHaveBean(RedisReactiveHealthIndicator.class)
						.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.redis.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(RedisHealthIndicator.class)
						.doesNotHaveBean(RedisReactiveHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
