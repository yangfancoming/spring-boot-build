

package org.springframework.boot.actuate.autoconfigure.metrics.export.wavefront;

import io.micrometer.wavefront.WavefrontConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WavefrontProperties}.
 *
 * @author Stephane Nicoll
 */
public class WavefrontPropertiesTests extends StepRegistryPropertiesTests {

	@Override
	public void defaultValuesAreConsistent() {
		WavefrontProperties properties = new WavefrontProperties();
		WavefrontConfig config = WavefrontConfig.DEFAULT_DIRECT;
		assertStepRegistryDefaultValues(properties, config);
		assertThat(properties.getUri().toString()).isEqualTo(config.uri());
		// source has no static default value
		assertThat(properties.getApiToken()).isEqualTo(config.apiToken());
		assertThat(properties.getGlobalPrefix()).isEqualTo(config.globalPrefix());
	}

}
