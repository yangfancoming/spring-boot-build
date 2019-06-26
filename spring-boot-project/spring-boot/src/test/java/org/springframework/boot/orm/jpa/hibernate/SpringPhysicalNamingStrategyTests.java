

package org.springframework.boot.orm.jpa.hibernate;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringPhysicalNamingStrategy}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
public class SpringPhysicalNamingStrategyTests {

	private Metadata metadata;

	private MetadataSources metadataSources;

	private StandardServiceRegistry serviceRegistry;

	@Before
	public void setup() {
		this.metadataSources = new MetadataSources();
		this.metadataSources.addAnnotatedClass(TelephoneNumber.class);
		this.serviceRegistry = getServiceRegistry(this.metadataSources);
		this.metadata = this.metadataSources.getMetadataBuilder(this.serviceRegistry)
				.applyPhysicalNamingStrategy(new SpringPhysicalNamingStrategy()).build();
	}

	private StandardServiceRegistry getServiceRegistry(MetadataSources metadataSources) {
		ServiceRegistry registry = metadataSources.getServiceRegistry();
		return new StandardServiceRegistryBuilder((BootstrapServiceRegistry) registry)
				.applySetting(AvailableSettings.DIALECT, H2Dialect.class).build();
	}

	@Test
	public void tableNameShouldBeLowercaseUnderscore() {
		PersistentClass binding = this.metadata
				.getEntityBinding(TelephoneNumber.class.getName());
		assertThat(binding.getTable().getQuotedName()).isEqualTo("telephone_number");
	}

	@Test
	public void tableNameShouldNotBeLowerCaseIfCaseSensitive() {
		this.metadata = this.metadataSources.getMetadataBuilder(this.serviceRegistry)
				.applyPhysicalNamingStrategy(new TestSpringPhysicalNamingStrategy())
				.build();
		PersistentClass binding = this.metadata
				.getEntityBinding(TelephoneNumber.class.getName());
		assertThat(binding.getTable().getQuotedName()).isEqualTo("Telephone_Number");
	}

	private class TestSpringPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

		@Override
		protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
			return false;
		}

	}

}
