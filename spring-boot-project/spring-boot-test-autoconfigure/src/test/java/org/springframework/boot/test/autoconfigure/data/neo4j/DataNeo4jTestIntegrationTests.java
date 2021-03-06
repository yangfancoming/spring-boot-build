

package org.springframework.boot.test.autoconfigure.data.neo4j;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.testsupport.testcontainers.Neo4jContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link DataNeo4jTest}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = DataNeo4jTestIntegrationTests.Initializer.class)
@DataNeo4jTest
public class DataNeo4jTestIntegrationTests {

	@ClassRule
	public static Neo4jContainer neo4j = new Neo4jContainer();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private Session session;

	@Autowired
	private ExampleRepository exampleRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testRepository() {
		ExampleGraph exampleGraph = new ExampleGraph();
		exampleGraph.setDescription("Look, new @DataNeo4jTest!");
		assertThat(exampleGraph.getId()).isNull();
		ExampleGraph savedGraph = this.exampleRepository.save(exampleGraph);
		assertThat(savedGraph.getId()).isNotNull();
		assertThat(this.session.countEntitiesOfType(ExampleGraph.class)).isEqualTo(1);
	}

	@Test
	public void didNotInjectExampleService() {
		this.thrown.expect(NoSuchBeanDefinitionException.class);
		this.applicationContext.getBean(ExampleService.class);
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(
				ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.data.neo4j.uri=bolt://localhost:" + neo4j.getMappedPort())
					.applyTo(configurableApplicationContext.getEnvironment());
		}

	}

}
