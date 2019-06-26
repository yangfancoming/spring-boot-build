

package org.springframework.boot.autoconfigure.hazelcast;

import java.io.IOException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Configuration for Hazelcast client.
 *
 * @author Vedran Pavic
 * @author Stephane Nicoll
 */
@ConditionalOnClass(HazelcastClient.class)
@ConditionalOnMissingBean(HazelcastInstance.class)
class HazelcastClientConfiguration {

	static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.client.config";

	@Configuration
	@ConditionalOnMissingBean(ClientConfig.class)
	@Conditional(ConfigAvailableCondition.class)
	static class HazelcastClientConfigFileConfiguration {

		@Bean
		public HazelcastInstance hazelcastInstance(HazelcastProperties properties)
				throws IOException {
			Resource config = properties.resolveConfigLocation();
			if (config != null) {
				return new HazelcastClientFactory(config).getHazelcastInstance();
			}
			return HazelcastClient.newHazelcastClient();
		}

	}

	@Configuration
	@ConditionalOnSingleCandidate(ClientConfig.class)
	static class HazelcastClientConfigConfiguration {

		@Bean
		public HazelcastInstance hazelcastInstance(ClientConfig config) {
			return new HazelcastClientFactory(config).getHazelcastInstance();
		}

	}

	/**
	 * {@link HazelcastConfigResourceCondition} that checks if the
	 * {@code spring.hazelcast.config} configuration key is defined.
	 */
	static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {

		ConfigAvailableCondition() {
			super(CONFIG_SYSTEM_PROPERTY, "file:./hazelcast-client.xml",
					"classpath:/hazelcast-client.xml");
		}

	}

}
