

package org.springframework.boot.autoconfigure.session;

import java.time.Duration;

import com.hazelcast.core.HazelcastInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.config.annotation.web.http.HazelcastHttpSessionConfiguration;

/**
 * Hazelcast backed session configuration.
 *
 * @author Tommy Ludwig
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Vedran Pavic
 */
@Configuration
@ConditionalOnClass(HazelcastSessionRepository.class)
@ConditionalOnMissingBean(SessionRepository.class)
@ConditionalOnBean(HazelcastInstance.class)
@Conditional(ServletSessionCondition.class)
@EnableConfigurationProperties(HazelcastSessionProperties.class)
class HazelcastSessionConfiguration {

	@Configuration
	public static class SpringBootHazelcastHttpSessionConfiguration
			extends HazelcastHttpSessionConfiguration {

		@Autowired
		public void customize(SessionProperties sessionProperties,
				HazelcastSessionProperties hazelcastSessionProperties) {
			Duration timeout = sessionProperties.getTimeout();
			if (timeout != null) {
				setMaxInactiveIntervalInSeconds((int) timeout.getSeconds());
			}
			setSessionMapName(hazelcastSessionProperties.getMapName());
			setHazelcastFlushMode(hazelcastSessionProperties.getFlushMode());
		}

	}

}
