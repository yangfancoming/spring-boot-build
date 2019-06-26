

package org.springframework.boot.autoconfigure.session;

import java.util.Arrays;

import org.junit.Test;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.session.SessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NonUniqueSessionRepositoryFailureAnalyzer}.
 *
 * @author Stephane Nicoll
 */
public class NonUniqueSessionRepositoryFailureAnalyzerTests {

	private final FailureAnalyzer analyzer = new NonUniqueSessionRepositoryFailureAnalyzer();

	@Test
	public void failureAnalysisWithMultipleCandidates() {
		FailureAnalysis analysis = analyzeFailure(createFailure(
				JdbcOperationsSessionRepository.class, HazelcastSessionRepository.class));
		assertThat(analysis).isNotNull();
		assertThat(analysis.getDescription()).contains(
				JdbcOperationsSessionRepository.class.getName(),
				HazelcastSessionRepository.class.getName());
		assertThat(analysis.getAction()).contains("spring.session.store-type");
	}

	@SafeVarargs
	private final Exception createFailure(
			Class<? extends SessionRepository<?>>... candidates) {
		return new NonUniqueSessionRepositoryException(Arrays.asList(candidates));
	}

	private FailureAnalysis analyzeFailure(Exception failure) {
		FailureAnalysis analysis = this.analyzer.analyze(failure);
		if (analysis != null) {
			new LoggingFailureAnalysisReporter().report(analysis);
		}
		return analysis;
	}

}
