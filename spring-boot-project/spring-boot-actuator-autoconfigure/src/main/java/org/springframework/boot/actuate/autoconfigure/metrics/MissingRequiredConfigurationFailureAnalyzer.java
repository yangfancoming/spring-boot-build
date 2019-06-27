

package org.springframework.boot.actuate.autoconfigure.metrics;

import io.micrometer.core.instrument.config.MissingRequiredConfigurationException;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * An {@link AbstractFailureAnalyzer} that performs analysis of failures caused by a
 * {@link MissingRequiredConfigurationException}.
 *
 * @author Andy Wilkinson
 */
class MissingRequiredConfigurationFailureAnalyzer
		extends AbstractFailureAnalyzer<MissingRequiredConfigurationException> {

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure,
			MissingRequiredConfigurationException cause) {
		StringBuilder description = new StringBuilder();
		description.append(cause.getMessage());
		if (!cause.getMessage().endsWith(".")) {
			description.append(".");
		}
		return new FailureAnalysis(description.toString(),
				"Update your application to provide the missing configuration.", cause);
	}

}
