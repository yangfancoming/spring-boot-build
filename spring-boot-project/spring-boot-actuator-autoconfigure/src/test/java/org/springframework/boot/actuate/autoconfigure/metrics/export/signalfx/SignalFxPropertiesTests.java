

package org.springframework.boot.actuate.autoconfigure.metrics.export.signalfx;

import io.micrometer.signalfx.SignalFxConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SignalFxProperties}.
 *
 * @author Stephane Nicoll
 */
public class SignalFxPropertiesTests extends StepRegistryPropertiesTests {

	@Override
	public void defaultValuesAreConsistent() {
		SignalFxProperties properties = new SignalFxProperties();
		SignalFxConfig config = SignalFxConfig.DEFAULT;
		assertStepRegistryDefaultValues(properties, config);
		// access token is mandatory
		assertThat(properties.getUri()).isEqualTo(config.uri());
		// source has no static default value
	}

}
