

package org.springframework.boot.test.autoconfigure.orm.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for {@link TestEntityManager}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see AutoConfigureTestEntityManager
 */
@Configuration
@ConditionalOnClass({ EntityManagerFactory.class })
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class TestEntityManagerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TestEntityManager testEntityManager(
			EntityManagerFactory entityManagerFactory) {
		return new TestEntityManager(entityManagerFactory);
	}

}
