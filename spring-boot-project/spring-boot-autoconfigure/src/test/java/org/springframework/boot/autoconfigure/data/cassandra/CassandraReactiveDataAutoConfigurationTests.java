

package org.springframework.boot.autoconfigure.data.cassandra;

import java.util.Set;

import com.datastax.driver.core.Session;
import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.city.City;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CassandraReactiveDataAutoConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Mark Paluch
 */
public class CassandraReactiveDataAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void templateExists() {
		load("spring.data.cassandra.keyspaceName:boot_test");
		assertThat(this.context.getBeanNamesForType(ReactiveCassandraTemplate.class))
				.hasSize(1);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void entityScanShouldSetInitialEntitySet() {
		load(EntityScanConfig.class, "spring.data.cassandra.keyspaceName:boot_test");
		CassandraMappingContext mappingContext = this.context
				.getBean(CassandraMappingContext.class);
		Set<Class<?>> initialEntitySet = (Set<Class<?>>) ReflectionTestUtils
				.getField(mappingContext, "initialEntitySet");
		assertThat(initialEntitySet).containsOnly(City.class);
	}

	@Test
	public void userTypeResolverShouldBeSet() {
		load("spring.data.cassandra.keyspaceName:boot_test");
		CassandraMappingContext mappingContext = this.context
				.getBean(CassandraMappingContext.class);
		assertThat(ReflectionTestUtils.getField(mappingContext, "userTypeResolver"))
				.isInstanceOf(SimpleUserTypeResolver.class);
	}

	private void load(String... environment) {
		load(null, environment);
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		TestPropertyValues.of(environment).applyTo(ctx);
		if (config != null) {
			ctx.register(config);
		}
		ctx.register(TestConfiguration.class, CassandraAutoConfiguration.class,
				CassandraDataAutoConfiguration.class,
				CassandraReactiveDataAutoConfiguration.class);
		ctx.refresh();
		this.context = ctx;
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public Session session() {
			return mock(Session.class);
		}

	}

	@Configuration
	@EntityScan("org.springframework.boot.autoconfigure.data.cassandra.city")
	static class EntityScanConfig {

	}

}