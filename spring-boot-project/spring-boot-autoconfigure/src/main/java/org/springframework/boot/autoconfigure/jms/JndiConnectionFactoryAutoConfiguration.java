

package org.springframework.boot.autoconfigure.jms;

import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration.JndiOrPropertyCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.util.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for JMS provided from JNDI.
 *
 * @author Phillip Webb
 * @since 1.2.0
 */
@Configuration
@AutoConfigureBefore(JmsAutoConfiguration.class)
@ConditionalOnClass(JmsTemplate.class)
@ConditionalOnMissingBean(ConnectionFactory.class)
@Conditional(JndiOrPropertyCondition.class)
@EnableConfigurationProperties(JmsProperties.class)
public class JndiConnectionFactoryAutoConfiguration {

	// Keep these in sync with the condition below
	private static final String[] JNDI_LOCATIONS = { "java:/JmsXA",
			"java:/XAConnectionFactory" };

	private final JmsProperties properties;

	public JndiConnectionFactoryAutoConfiguration(JmsProperties properties) {
		this.properties = properties;
	}

	@Bean
	public ConnectionFactory connectionFactory() throws NamingException {
		if (StringUtils.hasLength(this.properties.getJndiName())) {
			return new JndiLocatorDelegate().lookup(this.properties.getJndiName(),
					ConnectionFactory.class);
		}
		return findJndiConnectionFactory();
	}

	private ConnectionFactory findJndiConnectionFactory() {
		for (String name : JNDI_LOCATIONS) {
			try {
				return new JndiLocatorDelegate().lookup(name, ConnectionFactory.class);
			}
			catch (NamingException ex) {
				// Swallow and continue
			}
		}
		throw new IllegalStateException(
				"Unable to find ConnectionFactory in JNDI locations "
						+ Arrays.asList(JNDI_LOCATIONS));
	}

	/**
	 * Condition for JNDI name or a specific property.
	 */
	static class JndiOrPropertyCondition extends AnyNestedCondition {

		JndiOrPropertyCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnJndi({ "java:/JmsXA", "java:/XAConnectionFactory" })
		static class Jndi {

		}

		@ConditionalOnProperty(prefix = "spring.jms", name = "jndi-name")
		static class Property {

		}

	}

}
