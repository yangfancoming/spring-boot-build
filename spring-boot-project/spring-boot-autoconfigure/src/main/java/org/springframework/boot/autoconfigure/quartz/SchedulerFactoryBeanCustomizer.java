

package org.springframework.boot.autoconfigure.quartz;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Callback interface that can be implemented by beans wishing to customize the Quartz
 * {@link SchedulerFactoryBean} before it is fully initialized, in particular to tune its
 * configuration.
 *
 * @author Vedran Pavic
 * @since 2.0.0
 */
@FunctionalInterface
public interface SchedulerFactoryBeanCustomizer {

	/**
	 * Customize the {@link SchedulerFactoryBean}.
	 * @param schedulerFactoryBean the scheduler to customize
	 */
	void customize(SchedulerFactoryBean schedulerFactoryBean);

}
