

package org.springframework.boot.autoconfigure.security.reactive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.WebFilterChainProxy;

/**
 * Switches on {@link EnableWebFluxSecurity} for a reactive web application if this
 * annotation has not been added by the user. It delegates to Spring Security's
 * content-negotiation mechanism for authentication. This configuration also backs off if
 * a bean of type {@link WebFilterChainProxy} has been configured in any other way.
 *
 * @author Madhura Bhave
 */
@ConditionalOnClass({ EnableWebFluxSecurity.class, WebFilterChainProxy.class })
@ConditionalOnMissingBean(WebFilterChainProxy.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class WebFluxSecurityConfiguration {

	@Configuration
	@EnableWebFluxSecurity
	class EnableWebFluxSecurityConfiguration {

	}

}
