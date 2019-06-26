

package org.springframework.boot.autoconfigure.data.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;

/**
 * {@link BeanFactoryPostProcessor} that can be used to dynamically declare that all
 * {@link EntityManagerFactory} beans should "depend on" one or more specific beans.
 *
 * @author Marcel Overdijk
 * @author Dave Syer
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 1.1.0
 * @see BeanDefinition#setDependsOn(String[])
 */
public class EntityManagerFactoryDependsOnPostProcessor
		extends AbstractDependsOnBeanFactoryPostProcessor {

	public EntityManagerFactoryDependsOnPostProcessor(String... dependsOn) {
		super(EntityManagerFactory.class, AbstractEntityManagerFactoryBean.class,
				dependsOn);
	}

}
