

package org.springframework.boot.test.autoconfigure.data.redis;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.testsupport.testcontainers.RedisContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test with custom include filter for {@link DataRedisTest}.
 *
 * @author Jayaram Pradhan
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = DataRedisTestWithIncludeFilterIntegrationTests.Initializer.class)
@DataRedisTest(includeFilters = @Filter(Service.class))
public class DataRedisTestWithIncludeFilterIntegrationTests {

	@ClassRule
	public static RedisContainer redis = new RedisContainer();

	@Autowired
	private ExampleRepository exampleRepository;

	@Autowired
	private ExampleService service;

	@Test
	public void testService() {
		PersonHash personHash = new PersonHash();
		personHash.setDescription("Look, new @DataRedisTest!");
		assertThat(personHash.getId()).isNull();
		PersonHash savedEntity = this.exampleRepository.save(personHash);
		assertThat(this.service.hasRecord(savedEntity)).isTrue();
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(
				ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of("spring.redis.port=" + redis.getMappedPort())
					.applyTo(configurableApplicationContext.getEnvironment());
		}

	}

}
