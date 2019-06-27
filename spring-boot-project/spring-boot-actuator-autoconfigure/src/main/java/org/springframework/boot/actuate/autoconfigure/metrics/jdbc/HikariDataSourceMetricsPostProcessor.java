

package org.springframework.boot.actuate.autoconfigure.metrics.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

/**
 * {@link BeanPostProcessor} that configures Hikari metrics. Such arrangement is necessary
 * because a {@link HikariDataSource} instance cannot be modified once its configuration
 * has completed.
 *
 * @author Stephane Nicoll
 */
class HikariDataSourceMetricsPostProcessor implements BeanPostProcessor, Ordered {

	private static final Log logger = LogFactory
			.getLog(HikariDataSourceMetricsPostProcessor.class);

	private final ApplicationContext context;

	private volatile MeterRegistry meterRegistry;

	HikariDataSourceMetricsPostProcessor(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if (bean instanceof HikariDataSource) {
			bindMetricsRegistryToHikariDataSource(getMeterRegistry(),
					(HikariDataSource) bean);
		}
		return bean;
	}

	private void bindMetricsRegistryToHikariDataSource(MeterRegistry registry,
			HikariDataSource dataSource) {
		if (dataSource.getMetricRegistry() == null
				&& dataSource.getMetricsTrackerFactory() == null) {
			try {
				dataSource.setMetricsTrackerFactory(
						new MicrometerMetricsTrackerFactory(registry));
			}
			catch (Exception ex) {
				logger.warn("Failed to bind Hikari metrics: " + ex.getMessage());
			}
		}
	}

	private MeterRegistry getMeterRegistry() {
		if (this.meterRegistry == null) {
			this.meterRegistry = this.context.getBean(MeterRegistry.class);
		}
		return this.meterRegistry;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
