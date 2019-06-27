

package org.springframework.boot.autoconfigure.data.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.alt.redis.CityRedisRepository;
import org.springframework.boot.autoconfigure.data.empty.EmptyDataPackage;
import org.springframework.boot.autoconfigure.data.redis.city.City;
import org.springframework.boot.autoconfigure.data.redis.city.CityRepository;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.testsupport.testcontainers.RedisContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedisRepositoriesAutoConfiguration}.
 *
 * @author Eddú Meléndez
 */
public class RedisRepositoriesAutoConfigurationTests {

	@ClassRule
	public static RedisContainer redis = new RedisContainer();

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	@Before
	public void setUp() {
		TestPropertyValues.of("spring.redis.port=" + redis.getMappedPort())
				.applyTo(this.context.getEnvironment());
	}

	@After
	public void close() {
		this.context.close();
	}

	@Test
	public void testDefaultRepositoryConfiguration() {
		this.context.register(TestConfiguration.class, RedisAutoConfiguration.class,
				RedisRepositoriesAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean(CityRepository.class)).isNotNull();
	}

	@Test
	public void testNoRepositoryConfiguration() {
		this.context.register(EmptyConfiguration.class, RedisAutoConfiguration.class,
				RedisRepositoriesAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean("redisTemplate")).isNotNull();
	}

	@Test
	public void doesNotTriggerDefaultRepositoryDetectionIfCustomized() {
		this.context.register(CustomizedConfiguration.class, RedisAutoConfiguration.class,
				RedisRepositoriesAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean(CityRedisRepository.class)).isNotNull();
	}

	@Configuration
	@TestAutoConfigurationPackage(City.class)
	protected static class TestConfiguration {

	}

	@Configuration
	@TestAutoConfigurationPackage(EmptyDataPackage.class)
	protected static class EmptyConfiguration {

	}

	@Configuration
	@TestAutoConfigurationPackage(RedisRepositoriesAutoConfigurationTests.class)
	@EnableRedisRepositories(basePackageClasses = CityRedisRepository.class)
	static class CustomizedConfiguration {

	}

}
