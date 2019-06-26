

package org.springframework.boot.diagnostics.analyzer;

import javax.validation.ValidationException;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * A {@link FailureAnalyzer} that performs analysis of failures caused by a
 * {@link ValidationException}.
 *
 * @author Andy Wilkinson
 */
class ValidationExceptionFailureAnalyzer
		extends AbstractFailureAnalyzer<ValidationException> {

	private static final String MISSING_IMPLEMENTATION_MESSAGE = "Unable to create a "
			+ "Configuration, because no Bean Validation provider could be found";

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, ValidationException cause) {
		if (cause.getMessage().startsWith(MISSING_IMPLEMENTATION_MESSAGE)) {
			return new FailureAnalysis(
					"The Bean Validation API is on the classpath but no implementation"
							+ " could be found",
					"Add an implementation, such as Hibernate Validator, to the"
							+ " classpath",
					cause);
		}
		return null;
	}

}
