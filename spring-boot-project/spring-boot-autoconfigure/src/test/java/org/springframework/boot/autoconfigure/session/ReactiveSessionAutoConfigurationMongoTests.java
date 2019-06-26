

package org.springframework.boot.autoconfigure.session;

import org.junit.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.session.data.mongo.ReactiveMongoOperationsSessionRepository;
import org.springframework.session.data.redis.ReactiveRedisOperationsSessionRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Mongo-specific tests for {@link SessionAutoConfiguration}.
 *
 * @author Andy Wilkinson
 */
public class ReactiveSessionAutoConfigurationMongoTests
		extends AbstractSessionAutoConfigurationTests {

	private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(SessionAutoConfiguration.class));

	@Test
	public void defaultConfig() {
		this.contextRunner.withPropertyValues("spring.session.store-type=mongodb")
				.withConfiguration(AutoConfigurations.of(
						EmbeddedMongoAutoConfiguration.class,
						MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
						MongoReactiveAutoConfiguration.class,
						MongoReactiveDataAutoConfiguration.class))
				.run(validateSpringSessionUsesMongo("sessions"));
	}

	@Test
	public void defaultConfigWithUniqueStoreImplementation() {
		this.contextRunner
				.withClassLoader(new FilteredClassLoader(
						ReactiveRedisOperationsSessionRepository.class))
				.withConfiguration(AutoConfigurations.of(
						EmbeddedMongoAutoConfiguration.class,
						MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
						MongoReactiveAutoConfiguration.class,
						MongoReactiveDataAutoConfiguration.class))
				.run(validateSpringSessionUsesMongo("sessions"));
	}

	@Test
	public void mongoSessionStoreWithCustomizations() {
		this.contextRunner
				.withConfiguration(AutoConfigurations.of(
						EmbeddedMongoAutoConfiguration.class,
						MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
						MongoReactiveAutoConfiguration.class,
						MongoReactiveDataAutoConfiguration.class))
				.withPropertyValues("spring.session.store-type=mongodb",
						"spring.session.mongodb.collection-name=foo")
				.run(validateSpringSessionUsesMongo("foo"));
	}

	private ContextConsumer<AssertableReactiveWebApplicationContext> validateSpringSessionUsesMongo(
			String collectionName) {
		return (context) -> {
			ReactiveMongoOperationsSessionRepository repository = validateSessionRepository(
					context, ReactiveMongoOperationsSessionRepository.class);
			assertThat(new DirectFieldAccessor(repository)
					.getPropertyValue("collectionName")).isEqualTo(collectionName);
		};
	}

}
