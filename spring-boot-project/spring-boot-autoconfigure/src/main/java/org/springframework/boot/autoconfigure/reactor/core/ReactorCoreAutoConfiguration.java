

package org.springframework.boot.autoconfigure.reactor.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Reactor Core.
 *
 * @author Brian Clozel
 * @author Eddú Meléndez
 */
@Configuration
@ConditionalOnClass({ Mono.class, Flux.class })
@EnableConfigurationProperties(ReactorCoreProperties.class)
public class ReactorCoreAutoConfiguration {

	@Autowired
	protected void initialize(ReactorCoreProperties properties) {
		if (properties.getStacktraceMode().isEnabled()) {
			Hooks.onOperatorDebug();
		}
	}

}
