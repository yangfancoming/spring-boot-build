

package org.springframework.boot.autoconfigure.data.jpa;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's JPA Repositories.
 * <p>
 * Activates when there is a bean of type {@link javax.sql.DataSource} configured in the
 * context, the Spring Data JPA
 * {@link org.springframework.data.jpa.repository.JpaRepository} type is on the classpath,
 * and there is no other, existing
 * {@link org.springframework.data.jpa.repository.JpaRepository} configured.
 * <p>
 * Once in effect, the auto-configuration is the equivalent of enabling JPA repositories
 * using the {@link org.springframework.data.jpa.repository.config.EnableJpaRepositories}
 * annotation.
 * <p>
 * This configuration class will activate <em>after</em> the Hibernate auto-configuration.
 *
 * @author Phillip Webb
 * @author Josh Long
 * @see EnableJpaRepositories
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass(JpaRepository.class)
@ConditionalOnMissingBean({ JpaRepositoryFactoryBean.class,
		JpaRepositoryConfigExtension.class })
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(JpaRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class JpaRepositoriesAutoConfiguration {

}
