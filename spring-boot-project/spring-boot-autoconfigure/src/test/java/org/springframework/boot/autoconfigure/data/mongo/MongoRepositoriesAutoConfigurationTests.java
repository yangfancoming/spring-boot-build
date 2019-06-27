

package org.springframework.boot.autoconfigure.data.mongo;

import java.util.Set;

import com.mongodb.MongoClient;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.alt.mongo.CityMongoDbRepository;
import org.springframework.boot.autoconfigure.data.empty.EmptyDataPackage;
import org.springframework.boot.autoconfigure.data.mongo.city.City;
import org.springframework.boot.autoconfigure.data.mongo.city.CityRepository;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MongoRepositoriesAutoConfiguration}.
 *
 * @author Dave Syer
 * @author Oliver Gierke
 */
public class MongoRepositoriesAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MongoAutoConfiguration.class,
					MongoDataAutoConfiguration.class,
					MongoRepositoriesAutoConfiguration.class,
					PropertyPlaceholderAutoConfiguration.class));

	@Test
	public void testDefaultRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.run((context) -> {
					assertThat(context).hasSingleBean(CityRepository.class);
					assertThat(context).hasSingleBean(MongoClient.class);
					MongoMappingContext mappingContext = context
							.getBean(MongoMappingContext.class);
					@SuppressWarnings("unchecked")
					Set<? extends Class<?>> entities = (Set<? extends Class<?>>) ReflectionTestUtils
							.getField(mappingContext, "initialEntitySet");
					assertThat(entities).hasSize(1);
				});
	}

	@Test
	public void testNoRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.run((context) -> assertThat(context).hasSingleBean(MongoClient.class));
	}

	@Test
	public void doesNotTriggerDefaultRepositoryDetectionIfCustomized() {
		this.contextRunner.withUserConfiguration(CustomizedConfiguration.class)
				.run((context) -> assertThat(context)
						.hasSingleBean(CityMongoDbRepository.class));
	}

	@Test
	public void autoConfigurationShouldNotKickInEvenIfManualConfigDidNotCreateAnyRepositories() {
		this.contextRunner.withUserConfiguration(SortOfInvalidCustomConfiguration.class)
				.run((context) -> assertThat(context)
						.doesNotHaveBean(CityRepository.class));
	}

	@Test
	public void enablingReactiveRepositoriesDisablesImperativeRepositories() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.data.mongodb.repositories.type=reactive")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(CityRepository.class));
	}

	@Test
	public void enablingNoRepositoriesDisablesImperativeRepositories() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.data.mongodb.repositories.type=none")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(CityRepository.class));
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
	@TestAutoConfigurationPackage(MongoRepositoriesAutoConfigurationTests.class)
	@EnableMongoRepositories(basePackageClasses = CityMongoDbRepository.class)
	protected static class CustomizedConfiguration {

	}

	@Configuration
	// To not find any repositories
	@EnableMongoRepositories("foo.bar")
	@TestAutoConfigurationPackage(MongoRepositoriesAutoConfigurationTests.class)
	protected static class SortOfInvalidCustomConfiguration {

	}

}
