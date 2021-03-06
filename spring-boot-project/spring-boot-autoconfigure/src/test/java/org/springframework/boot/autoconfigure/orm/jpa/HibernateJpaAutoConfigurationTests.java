

package org.springframework.boot.autoconfigure.orm.jpa;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.mapping.NonAnnotatedEntity;
import org.springframework.boot.autoconfigure.orm.jpa.test.City;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link HibernateJpaAutoConfiguration}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Kazuki Shimizu
 * @author Stephane Nicoll
 */
public class HibernateJpaAutoConfigurationTests
		extends AbstractJpaAutoConfigurationTests {

	public HibernateJpaAutoConfigurationTests() {
		super(HibernateJpaAutoConfiguration.class);
	}

	@Test
	public void testDataScriptWithMissingDdl() {
		contextRunner().withPropertyValues("spring.datasource.data:classpath:/city.sql",
				// Missing:
				"spring.datasource.schema:classpath:/ddl.sql").run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context.getStartupFailure())
							.hasMessageContaining("ddl.sql");
					assertThat(context.getStartupFailure())
							.hasMessageContaining("spring.datasource.schema");
				});
	}

	@Test
	public void testDataScript() {
		// This can't succeed because the data SQL is executed immediately after the
		// schema
		// and Hibernate hasn't initialized yet at that point
		contextRunner().withPropertyValues("spring.datasource.data:classpath:/city.sql")
				.run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context.getStartupFailure())
							.isInstanceOf(BeanCreationException.class);
				});
	}

	@Test
	public void testDataScriptRunsEarly() {
		contextRunner().withUserConfiguration(TestInitializedJpaConfiguration.class)
				.withClassLoader(new HideDataScriptClassLoader())
				.withPropertyValues("spring.jpa.show-sql=true",
						"spring.jpa.hibernate.ddl-auto:create-drop",
						"spring.datasource.data:classpath:/city.sql")
				.run((context) -> assertThat(
						context.getBean(TestInitializedJpaConfiguration.class).called)
								.isTrue());
	}

	@Test
	public void testFlywaySwitchOffDdlAuto() {
		contextRunner()
				.withPropertyValues("spring.datasource.initialization-mode:never",
						"spring.flyway.locations:classpath:db/city")
				.withConfiguration(AutoConfigurations.of(FlywayAutoConfiguration.class))
				.run((context) -> assertThat(context).hasNotFailed());
	}

	@Test
	public void testFlywayPlusValidation() {
		contextRunner()
				.withPropertyValues("spring.datasource.initialization-mode:never",
						"spring.flyway.locations:classpath:db/city",
						"spring.jpa.hibernate.ddl-auto:validate")
				.withConfiguration(AutoConfigurations.of(FlywayAutoConfiguration.class))
				.run((context) -> assertThat(context).hasNotFailed());
	}

	@Test
	public void testLiquibasePlusValidation() {
		contextRunner().withPropertyValues("spring.datasource.initialization-mode:never",
				"spring.liquibase.changeLog:classpath:db/changelog/db.changelog-city.yaml",
				"spring.jpa.hibernate.ddl-auto:validate")
				.withConfiguration(
						AutoConfigurations.of(LiquibaseAutoConfiguration.class))
				.run((context) -> assertThat(context).hasNotFailed());
	}

	@Test
	public void jtaDefaultPlatform() {
		contextRunner()
				.withConfiguration(AutoConfigurations.of(JtaAutoConfiguration.class))
				.run((context) -> {
					Map<String, Object> jpaPropertyMap = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat(jpaPropertyMap.get("hibernate.transaction.jta.platform"))
							.isInstanceOf(SpringJtaPlatform.class);
				});
	}

	@Test
	public void jtaCustomPlatform() {
		contextRunner()
				.withPropertyValues(
						"spring.jpa.properties.hibernate.transaction.jta.platform:"
								+ TestJtaPlatform.class.getName())
				.withConfiguration(AutoConfigurations.of(JtaAutoConfiguration.class))
				.run((context) -> {
					Map<String, Object> jpaPropertyMap = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat((String) jpaPropertyMap
							.get("hibernate.transaction.jta.platform"))
									.isEqualTo(TestJtaPlatform.class.getName());
				});
	}

	@Test
	public void jtaCustomTransactionManagerUsingProperties() {
		contextRunner()
				.withPropertyValues("spring.transaction.default-timeout:30",
						"spring.transaction.rollback-on-commit-failure:true")
				.run((context) -> {
					JpaTransactionManager transactionManager = context
							.getBean(JpaTransactionManager.class);
					assertThat(transactionManager.getDefaultTimeout()).isEqualTo(30);
					assertThat(transactionManager.isRollbackOnCommitFailure()).isTrue();
				});
	}

	@Test
	public void autoConfigurationBacksOffWithSeveralDataSources() {
		contextRunner()
				.withConfiguration(AutoConfigurations.of(
						DataSourceTransactionManagerAutoConfiguration.class,
						XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class))
				.withUserConfiguration(TestTwoDataSourcesConfiguration.class)
				.run((context) -> {
					assertThat(context).hasNotFailed();
					assertThat(context).doesNotHaveBean(EntityManagerFactory.class);
				});
	}

	@Test
	public void providerDisablesAutoCommitIsConfigured() {
		contextRunner().withPropertyValues(
				"spring.datasource.type:" + HikariDataSource.class.getName(),
				"spring.datasource.hikari.auto-commit:false").run((context) -> {
					Map<String, Object> jpaProperties = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat(jpaProperties).contains(entry(
							"hibernate.connection.provider_disables_autocommit", "true"));
				});
	}

	@Test
	public void providerDisablesAutoCommitIsNotConfiguredIfAutoCommitIsEnabled() {
		contextRunner().withPropertyValues(
				"spring.datasource.type:" + HikariDataSource.class.getName(),
				"spring.datasource.hikari.auto-commit:true").run((context) -> {
					Map<String, Object> jpaProperties = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat(jpaProperties).doesNotContainKeys(
							"hibernate.connection.provider_disables_autocommit");
				});
	}

	@Test
	public void providerDisablesAutoCommitIsNotConfiguredIfPropertyIsSet() {
		contextRunner().withPropertyValues(
				"spring.datasource.type:" + HikariDataSource.class.getName(),
				"spring.datasource.hikari.auto-commit:false",
				"spring.jpa.properties.hibernate.connection.provider_disables_autocommit=false")
				.run((context) -> {
					Map<String, Object> jpaProperties = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat(jpaProperties).contains(
							entry("hibernate.connection.provider_disables_autocommit",
									"false"));
				});
	}

	@Test
	public void providerDisablesAutoCommitIsNotConfiguredWithJta() {
		contextRunner()
				.withConfiguration(AutoConfigurations.of(JtaAutoConfiguration.class))
				.withPropertyValues(
						"spring.datasource.type:" + HikariDataSource.class.getName(),
						"spring.datasource.hikari.auto-commit:false")
				.run((context) -> {
					Map<String, Object> jpaProperties = context
							.getBean(LocalContainerEntityManagerFactoryBean.class)
							.getJpaPropertyMap();
					assertThat(jpaProperties).doesNotContainKeys(
							"hibernate.connection.provider_disables_autocommit");
				});
	}

	@Test
	public void customResourceMapping() {
		contextRunner().withClassLoader(new HideDataScriptClassLoader())
				.withPropertyValues(
						"spring.datasource.data:classpath:/db/non-annotated-data.sql",
						"spring.jpa.mapping-resources=META-INF/mappings/non-annotated.xml")
				.run((context) -> {
					EntityManager em = context.getBean(EntityManagerFactory.class)
							.createEntityManager();
					NonAnnotatedEntity found = em.find(NonAnnotatedEntity.class, 2000L);
					assertThat(found).isNotNull();
					assertThat(found.getValue()).isEqualTo("Test");
				});
	}

	@Test
	public void physicalNamingStrategyCanBeUsed() {
		contextRunner()
				.withUserConfiguration(TestPhysicalNamingStrategyConfiguration.class)
				.run((context) -> {
					Map<String, Object> hibernateProperties = context
							.getBean(HibernateJpaConfiguration.class)
							.getVendorProperties();
					assertThat(hibernateProperties)
							.contains(entry("hibernate.physical_naming_strategy",
									context.getBean("testPhysicalNamingStrategy")));
					assertThat(hibernateProperties)
							.doesNotContainKeys("hibernate.ejb.naming_strategy");
				});
	}

	@Test
	public void implicitNamingStrategyCanBeUsed() {
		contextRunner()
				.withUserConfiguration(TestImplicitNamingStrategyConfiguration.class)
				.run((context) -> {
					Map<String, Object> hibernateProperties = context
							.getBean(HibernateJpaConfiguration.class)
							.getVendorProperties();
					assertThat(hibernateProperties)
							.contains(entry("hibernate.implicit_naming_strategy",
									context.getBean("testImplicitNamingStrategy")));
					assertThat(hibernateProperties)
							.doesNotContainKeys("hibernate.ejb.naming_strategy");
				});
	}

	@Test
	public void namingStrategyInstancesTakePrecedenceOverNamingStrategyProperties() {
		contextRunner()
				.withUserConfiguration(TestPhysicalNamingStrategyConfiguration.class,
						TestImplicitNamingStrategyConfiguration.class)
				.withPropertyValues(
						"spring.jpa.hibernate.naming.physical-strategy:com.example.Physical",
						"spring.jpa.hibernate.naming.implicit-strategy:com.example.Implicit")
				.run((context) -> {
					Map<String, Object> hibernateProperties = context
							.getBean(HibernateJpaConfiguration.class)
							.getVendorProperties();
					assertThat(hibernateProperties).contains(
							entry("hibernate.physical_naming_strategy",
									context.getBean("testPhysicalNamingStrategy")),
							entry("hibernate.implicit_naming_strategy",
									context.getBean("testImplicitNamingStrategy")));
					assertThat(hibernateProperties)
							.doesNotContainKeys("hibernate.ejb.naming_strategy");
				});
	}

	@Test
	public void hibernatePropertiesCustomizerTakesPrecedenceOverStrategyInstancesAndNamingStrategyProperties() {
		contextRunner()
				.withUserConfiguration(
						TestHibernatePropertiesCustomizerConfiguration.class,
						TestPhysicalNamingStrategyConfiguration.class,
						TestImplicitNamingStrategyConfiguration.class)
				.withPropertyValues(
						"spring.jpa.hibernate.naming.physical-strategy:com.example.Physical",
						"spring.jpa.hibernate.naming.implicit-strategy:com.example.Implicit")
				.run((context) -> {
					Map<String, Object> hibernateProperties = context
							.getBean(HibernateJpaConfiguration.class)
							.getVendorProperties();
					TestHibernatePropertiesCustomizerConfiguration configuration = context
							.getBean(
									TestHibernatePropertiesCustomizerConfiguration.class);
					assertThat(hibernateProperties).contains(
							entry("hibernate.physical_naming_strategy",
									configuration.physicalNamingStrategy),
							entry("hibernate.implicit_naming_strategy",
									configuration.implicitNamingStrategy));
					assertThat(hibernateProperties)
							.doesNotContainKeys("hibernate.ejb.naming_strategy");
				});
	}

	@Configuration
	@TestAutoConfigurationPackage(City.class)
	static class TestInitializedJpaConfiguration {

		private boolean called;

		@Autowired
		public void validateDataSourceIsInitialized(
				EntityManagerFactory entityManagerFactory) {
			// Inject the entity manager to validate it is initialized at the injection
			// point
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			City city = entityManager.find(City.class, 2000L);
			assertThat(city).isNotNull();
			assertThat(city.getName()).isEqualTo("Washington");
			this.called = true;
		}

	}

	@Configuration
	static class TestImplicitNamingStrategyConfiguration {

		@Bean
		public ImplicitNamingStrategy testImplicitNamingStrategy() {
			return new SpringImplicitNamingStrategy();
		}

	}

	@Configuration
	static class TestPhysicalNamingStrategyConfiguration {

		@Bean
		public PhysicalNamingStrategy testPhysicalNamingStrategy() {
			return new SpringPhysicalNamingStrategy();
		}

	}

	@Configuration
	static class TestHibernatePropertiesCustomizerConfiguration {

		private final PhysicalNamingStrategy physicalNamingStrategy = new SpringPhysicalNamingStrategy();

		private final ImplicitNamingStrategy implicitNamingStrategy = new SpringImplicitNamingStrategy();

		@Bean
		public HibernatePropertiesCustomizer testHibernatePropertiesCustomizer() {
			return (hibernateProperties) -> {
				hibernateProperties.put("hibernate.physical_naming_strategy",
						this.physicalNamingStrategy);
				hibernateProperties.put("hibernate.implicit_naming_strategy",
						this.implicitNamingStrategy);
			};
		}

	}

	public static class TestJtaPlatform implements JtaPlatform {

		@Override
		public TransactionManager retrieveTransactionManager() {
			return mock(TransactionManager.class);
		}

		@Override
		public UserTransaction retrieveUserTransaction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getTransactionIdentifier(Transaction transaction) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean canRegisterSynchronization() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void registerSynchronization(Synchronization synchronization) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCurrentStatus() {
			throw new UnsupportedOperationException();
		}

	}

	private static class HideDataScriptClassLoader extends URLClassLoader {

		private static final List<String> HIDDEN_RESOURCES = Arrays
				.asList("schema-all.sql", "schema.sql");

		HideDataScriptClassLoader() {
			super(new URL[0], HideDataScriptClassLoader.class.getClassLoader());
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			if (HIDDEN_RESOURCES.contains(name)) {
				return Collections.emptyEnumeration();
			}
			return super.getResources(name);
		}

	}

}
