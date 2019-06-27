

package org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus;

import io.micrometer.prometheus.PrometheusConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PrometheusProperties}.
 *
 * @author Stephane Nicoll
 */
public class PrometheusPropertiesTests {

	@Test
	public void defaultValuesAreConsistent() {
		PrometheusProperties properties = new PrometheusProperties();
		PrometheusConfig config = PrometheusConfig.DEFAULT;
		assertThat(properties.isDescriptions()).isEqualTo(config.descriptions());
		assertThat(properties.getStep()).isEqualTo(config.step());
	}

}
