

package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.core.HazelcastInstance;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Hazelcast. Creates a
 * {@link HazelcastInstance} based on explicit configuration or when a default
 * configuration file is found in the environment.
 *
 * @author Stephane Nicoll
 * @author Vedran Pavic
 * @since 1.3.0
 * @see HazelcastConfigResourceCondition
 */
@Configuration
@ConditionalOnClass(HazelcastInstance.class)
@EnableConfigurationProperties(HazelcastProperties.class)
@Import({ HazelcastClientConfiguration.class, HazelcastServerConfiguration.class })
public class HazelcastAutoConfiguration {

}
