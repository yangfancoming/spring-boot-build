

package org.springframework.boot.autoconfigure.data.couchbase;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseTestConfigurer;
import org.springframework.boot.autoconfigure.data.couchbase.city.City;
import org.springframework.boot.autoconfigure.data.couchbase.city.CityRepository;
import org.springframework.boot.autoconfigure.data.empty.EmptyDataPackage;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CouchbaseRepositoriesAutoConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
public class CouchbaseRepositoriesAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void couchbaseNotAvailable() {
		load(null);
		assertThat(this.context.getBeansOfType(CityRepository.class)).hasSize(0);
	}

	@Test
	public void defaultRepository() {
		load(DefaultConfiguration.class);
		assertThat(this.context.getBeansOfType(CityRepository.class)).hasSize(1);
	}

	@Test
	public void reactiveRepositories() {
		load(DefaultConfiguration.class,
				"spring.data.couchbase.repositories.type=reactive");
		assertThat(this.context.getBeansOfType(CityRepository.class)).hasSize(0);
	}

	@Test
	public void disabledRepositories() {
		load(DefaultConfiguration.class, "spring.data.couchbase.repositories.type=none");
		assertThat(this.context.getBeansOfType(CityRepository.class)).hasSize(0);
	}

	@Test
	public void noRepositoryAvailable() {
		load(NoRepositoryConfiguration.class);
		assertThat(this.context.getBeansOfType(CityRepository.class)).hasSize(0);
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(environment).applyTo(context);
		if (config != null) {
			context.register(config);
		}
		context.register(PropertyPlaceholderAutoConfiguration.class,
				CouchbaseAutoConfiguration.class, CouchbaseDataAutoConfiguration.class,
				CouchbaseRepositoriesAutoConfiguration.class);
		context.refresh();
		this.context = context;
	}

	@Configuration
	@TestAutoConfigurationPackage(City.class)
	static class CouchbaseNotAvailableConfiguration {

	}

	@Configuration
	@TestAutoConfigurationPackage(City.class)
	@Import(CouchbaseTestConfigurer.class)
	static class DefaultConfiguration {

	}

	@Configuration
	@TestAutoConfigurationPackage(EmptyDataPackage.class)
	@Import(CouchbaseTestConfigurer.class)
	protected static class NoRepositoryConfiguration {

	}

}
