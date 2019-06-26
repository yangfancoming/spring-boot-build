

package org.springframework.boot.jta.narayana;

import javax.transaction.TransactionManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * {@link BeanFactoryPostProcessor} to automatically setup correct beans ordering.
 *
 * @author Gytis Trikleris
 * @since 1.4.0
 */
public class NarayanaBeanFactoryPostProcessor
		implements BeanFactoryPostProcessor, Ordered {

	private static final String[] NO_BEANS = {};

	private static final int ORDER = Ordered.LOWEST_PRECEDENCE;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		String[] transactionManagers = beanFactory
				.getBeanNamesForType(TransactionManager.class, true, false);
		String[] recoveryManagers = beanFactory
				.getBeanNamesForType(NarayanaRecoveryManagerBean.class, true, false);
		addBeanDependencies(beanFactory, transactionManagers, "javax.sql.DataSource");
		addBeanDependencies(beanFactory, recoveryManagers, "javax.sql.DataSource");
		addBeanDependencies(beanFactory, transactionManagers,
				"javax.jms.ConnectionFactory");
		addBeanDependencies(beanFactory, recoveryManagers, "javax.jms.ConnectionFactory");
	}

	private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory,
			String[] beanNames, String dependencyType) {
		for (String beanName : beanNames) {
			addBeanDependencies(beanFactory, beanName, dependencyType);
		}
	}

	private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory,
			String beanName, String dependencyType) {
		for (String dependentBeanName : getBeanNamesForType(beanFactory,
				dependencyType)) {
			beanFactory.registerDependentBean(beanName, dependentBeanName);
		}
	}

	private String[] getBeanNamesForType(ConfigurableListableBeanFactory beanFactory,
			String type) {
		try {
			return beanFactory.getBeanNamesForType(Class.forName(type), true, false);
		}
		catch (ClassNotFoundException | NoClassDefFoundError ex) {
			// Ignore
		}
		return NO_BEANS;
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

}
