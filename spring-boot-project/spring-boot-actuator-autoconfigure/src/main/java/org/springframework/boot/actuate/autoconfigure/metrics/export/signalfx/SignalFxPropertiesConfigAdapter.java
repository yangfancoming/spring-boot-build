

package org.springframework.boot.actuate.autoconfigure.metrics.export.signalfx;

import io.micrometer.signalfx.SignalFxConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;

/**
 * Adapter to convert {@link SignalFxProperties} to a {@link SignalFxConfig}.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
public class SignalFxPropertiesConfigAdapter
		extends StepRegistryPropertiesConfigAdapter<SignalFxProperties>
		implements SignalFxConfig {

	public SignalFxPropertiesConfigAdapter(SignalFxProperties properties) {
		super(properties);
		accessToken(); // validate that an access token is set
	}

	@Override
	public String accessToken() {
		return get(SignalFxProperties::getAccessToken, SignalFxConfig.super::accessToken);
	}

	@Override
	public String uri() {
		return get(SignalFxProperties::getUri, SignalFxConfig.super::uri);
	}

	@Override
	public String source() {
		return get(SignalFxProperties::getSource, SignalFxConfig.super::source);
	}

}
