

package org.springframework.boot.test.autoconfigure.orm.jpa;

import javax.sql.DataSource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.AutoConfigurationImportedCondition.importedAutoConfiguration;

/**
 * Integration tests for {@link DataJpaTest}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.use-new-id-generator-mappings=false")
public class DataJpaTestIntegrationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private TestEntityManager entities;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ExampleRepository repository;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testEntityManager() {
		ExampleEntity entity = this.entities.persist(new ExampleEntity("spring", "123"));
		this.entities.flush();
		Object id = this.entities.getId(entity);
		ExampleEntity found = this.entities.find(ExampleEntity.class, id);
		assertThat(found.getName()).isEqualTo("spring");
	}

	@Test
	public void testEntityManagerPersistAndGetId() {
		Long id = this.entities.persistAndGetId(new ExampleEntity("spring", "123"),
				Long.class);
		assertThat(id).isNotNull();
		String reference = this.jdbcTemplate.queryForObject(
				"SELECT REFERENCE FROM EXAMPLE_ENTITY WHERE ID = ?", new Object[] { id },
				String.class);
		assertThat(reference).isEqualTo("123");
	}

	@Test
	public void testRepository() {
		this.entities.persist(new ExampleEntity("spring", "123"));
		this.entities.persist(new ExampleEntity("boot", "124"));
		this.entities.flush();
		ExampleEntity found = this.repository.findByReference("124");
		assertThat(found.getName()).isEqualTo("boot");
	}

	@Test
	public void replacesDefinedDataSourceWithEmbeddedDefault() throws Exception {
		String product = this.dataSource.getConnection().getMetaData()
				.getDatabaseProductName();
		assertThat(product).isEqualTo("H2");
	}

	@Test
	public void didNotInjectExampleComponent() {
		this.thrown.expect(NoSuchBeanDefinitionException.class);
		this.applicationContext.getBean(ExampleComponent.class);
	}

	@Test
	public void flywayAutoConfigurationWasImported() {
		assertThat(this.applicationContext)
				.has(importedAutoConfiguration(FlywayAutoConfiguration.class));
	}

	@Test
	public void liquibaseAutoConfigurationWasImported() {
		assertThat(this.applicationContext)
				.has(importedAutoConfiguration(LiquibaseAutoConfiguration.class));
	}

}
