

package org.springframework.boot.autoconfigure.data.neo4j;

import org.neo4j.ogm.session.Neo4jSession;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.config.Neo4jRepositoryConfigurationExtension;
import org.springframework.data.neo4j.repository.support.Neo4jRepositoryFactoryBean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Neo4j
 * Repositories.
 * <p>
 * Activates when there is no bean of type {@link Neo4jRepositoryFactoryBean} configured
 * in the context, the Spring Data Neo4j {@link Neo4jRepository} type is on the classpath,
 * the Neo4j client driver API is on the classpath, and there is no other configured
 * {@link Neo4jRepository}.
 * <p>
 * Once in effect, the auto-configuration is the equivalent of enabling Neo4j repositories
 * using the {@link EnableNeo4jRepositories} annotation.
 *
 * @author Dave Syer
 * @author Oliver Gierke
 * @author Josh Long
 * @since 1.4.0
 * @see EnableNeo4jRepositories
 */
@Configuration
@ConditionalOnClass({ Neo4jSession.class, Neo4jRepository.class })
@ConditionalOnMissingBean({ Neo4jRepositoryFactoryBean.class,
		Neo4jRepositoryConfigurationExtension.class })
@ConditionalOnProperty(prefix = "spring.data.neo4j.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(Neo4jRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(Neo4jDataAutoConfiguration.class)
public class Neo4jRepositoriesAutoConfiguration {

}
