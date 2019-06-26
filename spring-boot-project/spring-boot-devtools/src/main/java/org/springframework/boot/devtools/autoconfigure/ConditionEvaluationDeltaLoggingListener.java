

package org.springframework.boot.devtools.autoconfigure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportMessage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * An {@link ApplicationListener} that logs the delta of condition evaluation across
 * restarts.
 *
 * @author Andy Wilkinson
 */
class ConditionEvaluationDeltaLoggingListener
		implements ApplicationListener<ApplicationReadyEvent> {

	private final Log logger = LogFactory.getLog(getClass());

	private static ConditionEvaluationReport previousReport;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		ConditionEvaluationReport report = event.getApplicationContext()
				.getBean(ConditionEvaluationReport.class);
		if (previousReport != null) {
			ConditionEvaluationReport delta = report.getDelta(previousReport);
			if (!delta.getConditionAndOutcomesBySource().isEmpty()
					|| !delta.getExclusions().isEmpty()
					|| !delta.getUnconditionalClasses().isEmpty()) {
				if (this.logger.isInfoEnabled()) {
					this.logger.info("Condition evaluation delta:"
							+ new ConditionEvaluationReportMessage(delta,
									"CONDITION EVALUATION DELTA"));
				}
			}
			else {
				this.logger.info("Condition evaluation unchanged");
			}
		}
		previousReport = report;
	}

}
