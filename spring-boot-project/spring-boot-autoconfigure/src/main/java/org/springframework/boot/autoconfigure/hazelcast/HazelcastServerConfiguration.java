

package org.springframework.boot.autoconfigure.hazelcast;

import java.io.IOException;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Configuration for Hazelcast server.
 *
 * @author Stephane Nicoll
 * @author Vedran Pavic
 */
@ConditionalOnMissingBean(HazelcastInstance.class)
class HazelcastServerConfiguration {

	static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.config";

	@Configuration
	@ConditionalOnMissingBean(Config.class)
	@Conditional(ConfigAvailableCondition.class)
	static class HazelcastServerConfigFileConfiguration {

		@Bean
		public HazelcastInstance hazelcastInstance(HazelcastProperties properties)
				throws IOException {
			Resource config = properties.resolveConfigLocation();
			if (config != null) {
				return new HazelcastInstanceFactory(config).getHazelcastInstance();
			}
			return Hazelcast.newHazelcastInstance();
		}

	}

	@Configuration
	@ConditionalOnSingleCandidate(Config.class)
	static class HazelcastServerConfigConfiguration {

		@Bean
		public HazelcastInstance hazelcastInstance(Config config) {
			return new HazelcastInstanceFactory(config).getHazelcastInstance();
		}

	}

	/**
	 * {@link HazelcastConfigResourceCondition} that checks if the
	 * {@code spring.hazelcast.config} configuration key is defined.
	 */
	static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {

		ConfigAvailableCondition() {
			super(CONFIG_SYSTEM_PROPERTY, "file:./hazelcast.xml",
					"classpath:/hazelcast.xml");
		}

	}

}
