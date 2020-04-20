

package org.springframework.boot.actuate.autoconfigure.solr;

import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.solr.SolrHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SolrHealthIndicatorAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class SolrHealthIndicatorAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(SolrAutoConfiguration.class,
					SolrHealthIndicatorAutoConfiguration.class,
					HealthIndicatorAutoConfiguration.class));

	@Test
	public void runShouldCreateIndicator() {
		this.contextRunner.run(
				(context) -> assertThat(context).hasSingleBean(SolrHealthIndicator.class)
						.doesNotHaveBean(ApplicationHealthIndicator.class));
	}

	@Test
	public void runWhenDisabledShouldNotCreateIndicator() {
		this.contextRunner.withPropertyValues("management.health.solr.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(SolrHealthIndicator.class)
						.hasSingleBean(ApplicationHealthIndicator.class));
	}

}
