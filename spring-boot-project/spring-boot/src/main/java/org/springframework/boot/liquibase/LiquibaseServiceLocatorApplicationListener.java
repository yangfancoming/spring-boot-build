

package org.springframework.boot.liquibase;

import liquibase.servicelocator.CustomResolverServiceLocator;
import liquibase.servicelocator.ServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.ClassUtils;

/**
 * {@link ApplicationListener} that replaces the liquibase {@link ServiceLocator} with a version that works with Spring Boot executable archives.
 */
public class LiquibaseServiceLocatorApplicationListener implements ApplicationListener<ApplicationStartingEvent> {

	private static final Log logger = LogFactory.getLog(LiquibaseServiceLocatorApplicationListener.class);

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		if (ClassUtils.isPresent("liquibase.servicelocator.CustomResolverServiceLocator",event.getSpringApplication().getClassLoader())) {
			new LiquibasePresent().replaceServiceLocator();
		}
	}

	/**
	 * Inner class to prevent class not found issues.
	 */
	private static class LiquibasePresent {

		public void replaceServiceLocator() {
			CustomResolverServiceLocator customResolverServiceLocator = new CustomResolverServiceLocator(new SpringPackageScanClassResolver(logger));
			customResolverServiceLocator.addPackageToScan(CommonsLoggingLiquibaseLogger.class.getPackage().getName());
			ServiceLocator.setInstance(customResolverServiceLocator);
			liquibase.logging.LogFactory.reset();
		}

	}

}
