

package org.springframework.boot.autoconfigure.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

/**
 * {@link ApplicationContextInitializer} that writes the {@link ConditionEvaluationReport}
 * to the log. Reports are logged at the {@link LogLevel#DEBUG DEBUG} level unless there
 * was a problem, in which case they are the {@link LogLevel#INFO INFO} level is used.
 * This initializer is not intended to be shared across multiple application context instances.
 */
public class ConditionEvaluationReportLoggingListener implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private final Log logger = LogFactory.getLog(getClass());

	private ConfigurableApplicationContext applicationContext;

	private ConditionEvaluationReport report;

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		applicationContext.addApplicationListener(new ConditionEvaluationReportListener());
		if (applicationContext instanceof GenericApplicationContext) {
			// Get the report early in case the context fails to load
			this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
		}
	}

	protected void onApplicationEvent(ApplicationEvent event) {
		ConfigurableApplicationContext initializerApplicationContext = this.applicationContext;
		if (event instanceof ContextRefreshedEvent) {
			if (((ApplicationContextEvent) event).getApplicationContext() == initializerApplicationContext) {
				logAutoConfigurationReport();
			}
		}
		else if (event instanceof ApplicationFailedEvent && ((ApplicationFailedEvent) event) .getApplicationContext() == initializerApplicationContext) {
			logAutoConfigurationReport(true);
		}
	}

	private void logAutoConfigurationReport() {
		logAutoConfigurationReport(!this.applicationContext.isActive());
	}

	public void logAutoConfigurationReport(boolean isCrashReport) {
		if (this.report == null) {
			if (this.applicationContext == null) {
				this.logger.info("Unable to provide the conditions report due to missing ApplicationContext");
				return;
			}
			this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
		}
		if (!this.report.getConditionAndOutcomesBySource().isEmpty()) {
			if (isCrashReport && this.logger.isInfoEnabled() && !this.logger.isDebugEnabled()) {
				this.logger.info(String.format("%n%nError starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled."));
			}
			if (this.logger.isDebugEnabled()) {
				this.logger.debug(new ConditionEvaluationReportMessage(this.report));
			}
		}
	}

	private class ConditionEvaluationReportListener implements GenericApplicationListener {

		@Override
		public int getOrder() {
			return Ordered.LOWEST_PRECEDENCE;
		}

		@Override
		public boolean supportsEventType(ResolvableType resolvableType) {
			Class<?> type = resolvableType.getRawClass();
			if (type == null) return false;
			return ContextRefreshedEvent.class.isAssignableFrom(type) || ApplicationFailedEvent.class.isAssignableFrom(type);
		}

		@Override
		public boolean supportsSourceType(Class<?> sourceType) {
			return true;
		}

		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			ConditionEvaluationReportLoggingListener.this.onApplicationEvent(event);
		}
	}

}
