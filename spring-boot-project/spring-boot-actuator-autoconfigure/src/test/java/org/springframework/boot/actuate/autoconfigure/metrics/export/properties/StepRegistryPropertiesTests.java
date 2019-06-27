

package org.springframework.boot.actuate.autoconfigure.metrics.export.properties;

import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base tests for {@link StepRegistryProperties} implementation.
 *
 * @author Stephane Nicoll
 */
public abstract class StepRegistryPropertiesTests {

	protected void assertStepRegistryDefaultValues(StepRegistryProperties properties,
			StepRegistryConfig config) {
		assertThat(properties.getStep()).isEqualTo(config.step());
		assertThat(properties.isEnabled()).isEqualTo(config.enabled());
		assertThat(properties.getConnectTimeout()).isEqualTo(config.connectTimeout());
		assertThat(properties.getReadTimeout()).isEqualTo(config.readTimeout());
		assertThat(properties.getNumThreads()).isEqualTo(config.numThreads());
		assertThat(properties.getBatchSize()).isEqualTo(config.batchSize());
	}

	@Test
	public abstract void defaultValuesAreConsistent();

}
