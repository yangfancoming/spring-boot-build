

package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.reactivestreams.client.MongoClient;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.mongodb.repository.config.ReactiveMongoRepositoryConfigurationExtension;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Mongo Reactive
 * Repositories.
 * <p>
 * Activates when there is no bean of type
 * {@link org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean}
 * configured in the context, the Spring Data Mongo {@link ReactiveMongoRepository} type
 * is on the classpath, the ReactiveStreams Mongo client driver API is on the classpath,
 * and there is no other configured {@link ReactiveMongoRepository}.
 * <p>
 * Once in effect, the auto-configuration is the equivalent of enabling Mongo repositories
 * using the {@link EnableReactiveMongoRepositories} annotation.
 *
 * @author Mark Paluch
 * @since 2.0.0
 * @see EnableReactiveMongoRepositories
 */
@Configuration
@ConditionalOnClass({ MongoClient.class, ReactiveMongoRepository.class })
@ConditionalOnMissingBean({ ReactiveMongoRepositoryFactoryBean.class,
		ReactiveMongoRepositoryConfigurationExtension.class })
@ConditionalOnRepositoryType(store = "mongodb", type = RepositoryType.REACTIVE)
@Import(MongoReactiveRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(MongoReactiveDataAutoConfiguration.class)
public class MongoReactiveRepositoriesAutoConfiguration {

}
