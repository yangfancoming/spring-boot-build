

package org.springframework.boot.autoconfigure.transaction.jta;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for JTA.
 *
 * @author Josh Long
 * @author Phillip Webb
 * @since 1.2.0
 */
@ConditionalOnClass(javax.transaction.Transaction.class)
@ConditionalOnProperty(prefix = "spring.jta", value = "enabled", matchIfMissing = true)
@AutoConfigureBefore({ XADataSourceAutoConfiguration.class,
		ActiveMQAutoConfiguration.class, ArtemisAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class })
@Import({ JndiJtaConfiguration.class, BitronixJtaConfiguration.class,
		AtomikosJtaConfiguration.class, NarayanaJtaConfiguration.class })
@EnableConfigurationProperties(JtaProperties.class)
public class JtaAutoConfiguration {

}
