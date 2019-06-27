

package org.springframework.boot.autoconfigure.data.cassandra;

import java.util.Collections;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ObjectUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CassandraDataAutoConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Mark Paluch
 * @author Stephane Nicoll
 */
public class CassandraDataAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void templateExists() {
		load(TestExcludeConfiguration.class);
		assertThat(this.context.getBeanNamesForType(CassandraTemplate.class).length)
				.isEqualTo(1);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void entityScanShouldSetInitialEntitySet() {
		load(EntityScanConfig.class);
		CassandraMappingContext mappingContext = this.context
				.getBean(CassandraMappingContext.class);
		Set<Class<?>> initialEntitySet = (Set<Class<?>>) ReflectionTestUtils
				.getField(mappingContext, "initialEntitySet");
		assertThat(initialEntitySet).containsOnly(City.class);
	}

	@Test
	public void userTypeResolverShouldBeSet() {
		load();
		CassandraMappingContext mappingContext = this.context
				.getBean(CassandraMappingContext.class);
		assertThat(ReflectionTestUtils.getField(mappingContext, "userTypeResolver"))
				.isInstanceOf(SimpleUserTypeResolver.class);
	}

	@Test
	public void defaultConversions() {
		load();
		CassandraTemplate template = this.context.getBean(CassandraTemplate.class);
		assertThat(template.getConverter().getConversionService().canConvert(Person.class,
				String.class)).isFalse();
	}

	@Test
	public void customConversions() {
		load(CustomConversionConfig.class);
		CassandraTemplate template = this.context.getBean(CassandraTemplate.class);
		assertThat(template.getConverter().getConversionService().canConvert(Person.class,
				String.class)).isTrue();

	}

	public void load(Class<?>... config) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		TestPropertyValues.of("spring.data.cassandra.keyspaceName:boot_test")
				.applyTo(ctx);
		if (!ObjectUtils.isEmpty(config)) {
			ctx.register(config);
		}
		ctx.register(TestConfiguration.class, CassandraAutoConfiguration.class,
				CassandraDataAutoConfiguration.class);
		ctx.refresh();
		this.context = ctx;
	}

	@Configuration
	@ComponentScan(excludeFilters = @ComponentScan.Filter(classes = {
			Session.class }, type = FilterType.ASSIGNABLE_TYPE))
	static class TestExcludeConfiguration {

	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public Session getObject() {
			return mock(Session.class);
		}

	}

	@Configuration
	@EntityScan("org.springframework.boot.autoconfigure.data.cassandra.city")
	static class EntityScanConfig {

	}

	@Configuration
	static class CustomConversionConfig {

		@Bean
		public CassandraCustomConversions myCassandraCustomConversions() {
			return new CassandraCustomConversions(
					Collections.singletonList(new MyConverter()));
		}

	}

	private static class MyConverter implements Converter<Person, String> {

		@Override
		public String convert(Person o) {
			return null;
		}

	}

	private static class Person {

	}

}
