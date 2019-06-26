

package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.MongoClient;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Mongo
 * Repositories.
 * <p>
 * Activates when there is no bean of type
 * {@link org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean}
 * configured in the context, the Spring Data Mongo
 * {@link org.springframework.data.mongodb.repository.MongoRepository} type is on the
 * classpath, the Mongo client driver API is on the classpath, and there is no other
 * configured {@link org.springframework.data.mongodb.repository.MongoRepository}.
 * <p>
 * Once in effect, the auto-configuration is the equivalent of enabling Mongo repositories
 * using the
 * {@link org.springframework.data.mongodb.repository.config.EnableMongoRepositories}
 * annotation.
 *
 * @author Dave Syer
 * @author Oliver Gierke
 * @author Josh Long
 * @see EnableMongoRepositories
 */
@Configuration
@ConditionalOnClass({ MongoClient.class, MongoRepository.class })
@ConditionalOnMissingBean({ MongoRepositoryFactoryBean.class,
		MongoRepositoryConfigurationExtension.class })
@ConditionalOnRepositoryType(store = "mongodb", type = RepositoryType.IMPERATIVE)
@Import(MongoRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(MongoDataAutoConfiguration.class)
public class MongoRepositoriesAutoConfiguration {

}
