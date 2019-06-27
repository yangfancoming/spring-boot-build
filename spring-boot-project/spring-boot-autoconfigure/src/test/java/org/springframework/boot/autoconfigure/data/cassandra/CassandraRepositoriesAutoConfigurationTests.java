

package org.springframework.boot.autoconfigure.data.cassandra;

import java.util.Set;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.alt.cassandra.CityCassandraRepository;
import org.springframework.boot.autoconfigure.data.cassandra.city.City;
import org.springframework.boot.autoconfigure.data.cassandra.city.CityRepository;
import org.springframework.boot.autoconfigure.data.empty.EmptyDataPackage;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CassandraRepositoriesAutoConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Mark Paluch
 * @author Stephane Nicoll
 */
public class CassandraRepositoriesAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(CassandraAutoConfiguration.class,
					CassandraRepositoriesAutoConfiguration.class,
					CassandraDataAutoConfiguration.class,
					PropertyPlaceholderAutoConfiguration.class));

	@Test
	public void testDefaultRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.run((context) -> {
					assertThat(context).hasSingleBean(CityRepository.class);
					assertThat(context).hasSingleBean(Cluster.class);
					assertThat(getInitialEntitySet(context)).hasSize(1);
				});
	}

	@Test
	public void testNoRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(TestExcludeConfiguration.class,
				EmptyConfiguration.class).run((context) -> {
					assertThat(context).hasSingleBean(Cluster.class);
					assertThat(getInitialEntitySet(context)).hasSize(1)
							.containsOnly(City.class);
				});
	}

	@Test
	public void doesNotTriggerDefaultRepositoryDetectionIfCustomized() {
		this.contextRunner.withUserConfiguration(TestExcludeConfiguration.class,
				CustomizedConfiguration.class).run((context) -> {
					assertThat(context).hasSingleBean(CityCassandraRepository.class);
					assertThat(getInitialEntitySet(context)).hasSize(1)
							.containsOnly(City.class);
				});
	}

	@Test
	public void enablingReactiveRepositoriesDisablesImperativeRepositories() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.data.cassandra.repositories.type=reactive")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(CityCassandraRepository.class));
	}

	@Test
	public void enablingNoRepositoriesDisablesImperativeRepositories() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
				.withPropertyValues("spring.data.cassandra.repositories.type=none")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(CityCassandraRepository.class));
	}

	@SuppressWarnings("unchecked")
	private Set<Class<?>> getInitialEntitySet(AssertableApplicationContext context) {
		CassandraMappingContext mappingContext = context
				.getBean(CassandraMappingContext.class);
		return (Set<Class<?>>) ReflectionTestUtils.getField(mappingContext,
				"initialEntitySet");
	}

	@Configuration
	@TestAutoConfigurationPackage(City.class)
	static class TestConfiguration {

		@Bean
		public Session session() {
			return mock(Session.class);
		}

	}

	@Configuration
	@TestAutoConfigurationPackage(EmptyDataPackage.class)
	static class EmptyConfiguration {

	}

	@Configuration
	@TestAutoConfigurationPackage(CassandraRepositoriesAutoConfigurationTests.class)
	@EnableCassandraRepositories(basePackageClasses = CityCassandraRepository.class)
	static class CustomizedConfiguration {

	}

	@Configuration
	@ComponentScan(excludeFilters = @ComponentScan.Filter(classes = {
			Session.class }, type = FilterType.ASSIGNABLE_TYPE))
	static class TestExcludeConfiguration {

	}

}
