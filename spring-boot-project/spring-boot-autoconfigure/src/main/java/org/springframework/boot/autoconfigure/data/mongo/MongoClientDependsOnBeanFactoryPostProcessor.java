

package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.MongoClient;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

/**
 * {@link BeanFactoryPostProcessor} to automatically set up the recommended
 * {@link BeanDefinition#setDependsOn(String[]) dependsOn} configuration for Mongo clients
 * when used embedded Mongo.
 *
 * @author Andy Wilkinson
 * @since 1.3.0
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class MongoClientDependsOnBeanFactoryPostProcessor
		extends AbstractDependsOnBeanFactoryPostProcessor {

	public MongoClientDependsOnBeanFactoryPostProcessor(String... dependsOn) {
		super(MongoClient.class, MongoClientFactoryBean.class, dependsOn);
	}

}
