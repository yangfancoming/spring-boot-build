

package org.springframework.boot.autoconfigure.data.redis;

import java.util.Map;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedisReactiveAutoConfiguration}.
 *
 * @author Stephane Nicoll
 */
public class RedisReactiveAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(RedisAutoConfiguration.class,
					RedisReactiveAutoConfiguration.class));

	@Test
	public void testDefaultRedisConfiguration() {
		this.contextRunner.run((context) -> {
			Map<String, ?> beans = context.getBeansOfType(ReactiveRedisTemplate.class);
			assertThat(beans).containsOnlyKeys("reactiveRedisTemplate");
		});
	}

}
