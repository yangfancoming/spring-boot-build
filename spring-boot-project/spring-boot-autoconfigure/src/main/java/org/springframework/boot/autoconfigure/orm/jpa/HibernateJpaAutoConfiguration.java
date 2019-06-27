

package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.HibernateEntityManagerCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.ClassUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Hibernate JPA.
 *
 * @author Phillip Webb
 * @author Josh Long
 * @author Manuel Doninger
 * @author Andy Wilkinson
 */
@Configuration
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class })
@Conditional(HibernateEntityManagerCondition.class)
@EnableConfigurationProperties(JpaProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
@Import(HibernateJpaConfiguration.class)
public class HibernateJpaAutoConfiguration {

	@Order(Ordered.HIGHEST_PRECEDENCE + 20)
	static class HibernateEntityManagerCondition extends SpringBootCondition {

		private static final String[] CLASS_NAMES = {
				"org.hibernate.ejb.HibernateEntityManager",
				"org.hibernate.jpa.HibernateEntityManager" };

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage
					.forCondition("HibernateEntityManager");
			for (String className : CLASS_NAMES) {
				if (ClassUtils.isPresent(className, context.getClassLoader())) {
					return ConditionOutcome
							.match(message.found("class").items(Style.QUOTE, className));
				}
			}
			return ConditionOutcome.noMatch(message.didNotFind("class", "classes")
					.items(Style.QUOTE, Arrays.asList(CLASS_NAMES)));
		}

	}

}
