

package org.springframework.boot.actuate.autoconfigure.mongo;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MongoHealthIndicatorAutoConfiguration}
 *
 * @author Phillip Webb
 */
public class MongoHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MongoAutoConfiguration.class,
					MongoDataAutoConfiguration.class,
					MongoHealthIndicatorAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(MongoHealthIndicator.class)
						.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.mongo.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(MongoHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
