

package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * {@link BeanFactoryPostProcessor} that can be used to dynamically declare that all
 * {@link JdbcOperations} beans should "depend on" one or more specific beans.
 *
 * @author Marcel Overdijk
 * @author Dave Syer
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 2.0.4
 * @see BeanDefinition#setDependsOn(String[])
 */
public class JdbcOperationsDependsOnPostProcessor
		extends AbstractDependsOnBeanFactoryPostProcessor {

	public JdbcOperationsDependsOnPostProcessor(String... dependsOn) {
		super(JdbcOperations.class, dependsOn);
	}

}
