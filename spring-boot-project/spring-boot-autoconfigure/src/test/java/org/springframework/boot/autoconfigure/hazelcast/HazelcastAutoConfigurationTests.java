

package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HazelcastAutoConfiguration} with full classpath.
 *
 * @author Stephane Nicoll
 */
public class HazelcastAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(HazelcastAutoConfiguration.class));

	@Test
	public void defaultConfigFile() {
		// no hazelcast-client.xml and hazelcast.xml is present in root classpath
		this.contextRunner.run((context) -> {
			Config config = context.getBean(HazelcastInstance.class).getConfig();
			assertThat(config.getConfigurationUrl())
					.isEqualTo(new ClassPathResource("hazelcast.xml").getURL());
		});
	}

}
