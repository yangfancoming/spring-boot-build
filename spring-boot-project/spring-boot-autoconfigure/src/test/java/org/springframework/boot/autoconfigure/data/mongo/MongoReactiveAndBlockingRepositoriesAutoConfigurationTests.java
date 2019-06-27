

package org.springframework.boot.autoconfigure.data.mongo;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.data.mongo.city.CityRepository;
import org.springframework.boot.autoconfigure.data.mongo.city.ReactiveCityRepository;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfigurationTests;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MongoRepositoriesAutoConfiguration} and
 * {@link MongoReactiveRepositoriesAutoConfiguration}.
 *
 * @author Mark Paluch
 */
public class MongoReactiveAndBlockingRepositoriesAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		this.context.close();
	}

	@Test
	public void shouldCreateInstancesForReactiveAndBlockingRepositories() {
		this.context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of("spring.datasource.initialization-mode:never")
				.applyTo(this.context);
		this.context.register(BlockingAndReactiveConfiguration.class,
				BaseConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean(CityRepository.class)).isNotNull();
		assertThat(this.context.getBean(ReactiveCityRepository.class)).isNotNull();
	}

	@Configuration
	@TestAutoConfigurationPackage(MongoAutoConfigurationTests.class)
	@EnableMongoRepositories(basePackageClasses = ReactiveCityRepository.class)
	@EnableReactiveMongoRepositories(basePackageClasses = ReactiveCityRepository.class)
	protected static class BlockingAndReactiveConfiguration {

	}

	@Configuration
	@Import(Registrar.class)
	protected static class BaseConfiguration {

	}

	protected static class Registrar implements ImportSelector {

		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			List<String> names = new ArrayList<>();
			for (Class<?> type : new Class<?>[] { MongoAutoConfiguration.class,
					MongoReactiveAutoConfiguration.class,
					MongoDataAutoConfiguration.class,
					MongoRepositoriesAutoConfiguration.class,
					MongoReactiveDataAutoConfiguration.class,
					MongoReactiveRepositoriesAutoConfiguration.class }) {
				names.add(type.getName());
			}
			return StringUtils.toStringArray(names);
		}

	}

}
