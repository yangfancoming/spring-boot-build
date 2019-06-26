

package org.springframework.boot.actuate.autoconfigure.ldap;

import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.ldap.LdapHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapOperations;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link LdapHealthIndicator}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(LdapOperations.class)
@ConditionalOnBean(LdapOperations.class)
@ConditionalOnEnabledHealthIndicator("ldap")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(LdapDataAutoConfiguration.class)
public class LdapHealthIndicatorAutoConfiguration extends
		CompositeHealthIndicatorConfiguration<LdapHealthIndicator, LdapOperations> {

	private final Map<String, LdapOperations> ldapOperations;

	public LdapHealthIndicatorAutoConfiguration(
			Map<String, LdapOperations> ldapOperations) {
		this.ldapOperations = ldapOperations;
	}

	@Bean
	@ConditionalOnMissingBean(name = "ldapHealthIndicator")
	public HealthIndicator ldapHealthIndicator() {
		return createHealthIndicator(this.ldapOperations);
	}

}
