

package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.city.City;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.testsupport.testcontainers.CassandraContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CassandraDataAutoConfiguration} that require a Cassandra instance.
 *
 * @author Mark Paluch
 * @author Stephane Nicoll
 */
public class CassandraDataAutoConfigurationIntegrationTests {

	@ClassRule
	public static CassandraContainer cassandra = new CassandraContainer();

	private AnnotationConfigApplicationContext context;

	@Before
	public void setUp() {
		this.context = new AnnotationConfigApplicationContext();
		TestPropertyValues
				.of("spring.data.cassandra.port=" + cassandra.getMappedPort(),
						"spring.data.cassandra.read-timeout=24000",
						"spring.data.cassandra.connect-timeout=10000")
				.applyTo(this.context.getEnvironment());
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void hasDefaultSchemaActionSet() {
		String cityPackage = City.class.getPackage().getName();
		AutoConfigurationPackages.register(this.context, cityPackage);
		this.context.register(CassandraAutoConfiguration.class,
				CassandraDataAutoConfiguration.class);
		this.context.refresh();

		CassandraSessionFactoryBean bean = this.context
				.getBean(CassandraSessionFactoryBean.class);
		assertThat(bean.getSchemaAction()).isEqualTo(SchemaAction.NONE);
	}

	@Test
	public void hasRecreateSchemaActionSet() {
		createTestKeyspaceIfNotExists();
		String cityPackage = City.class.getPackage().getName();
		AutoConfigurationPackages.register(this.context, cityPackage);
		TestPropertyValues
				.of("spring.data.cassandra.schemaAction=recreate_drop_unused",
						"spring.data.cassandra.keyspaceName=boot_test")
				.applyTo(this.context);
		this.context.register(CassandraAutoConfiguration.class,
				CassandraDataAutoConfiguration.class);
		this.context.refresh();
		CassandraSessionFactoryBean bean = this.context
				.getBean(CassandraSessionFactoryBean.class);
		assertThat(bean.getSchemaAction()).isEqualTo(SchemaAction.RECREATE_DROP_UNUSED);
	}

	private void createTestKeyspaceIfNotExists() {
		Cluster cluster = Cluster.builder().withPort(cassandra.getMappedPort())
				.addContactPoint("localhost").build();
		try (Session session = cluster.connect()) {
			session.execute("CREATE KEYSPACE IF NOT EXISTS boot_test"
					+ "  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
		}
	}

}
