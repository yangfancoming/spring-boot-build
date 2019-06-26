

package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.reactivestreams.client.MongoClient;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;

/**
 * {@link BeanFactoryPostProcessor} to automatically set up the recommended
 * {@link BeanDefinition#setDependsOn(String[]) dependsOn} configuration for Mongo clients
 * when used embedded Mongo.
 *
 * @author Mark Paluch
 * @since 2.0.0
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor
		extends AbstractDependsOnBeanFactoryPostProcessor {

	public ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor(
			String... dependsOn) {
		super(MongoClient.class, ReactiveMongoClientFactoryBean.class, dependsOn);
	}

}
