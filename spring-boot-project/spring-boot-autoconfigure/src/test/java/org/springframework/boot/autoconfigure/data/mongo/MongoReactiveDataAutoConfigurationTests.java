

package org.springframework.boot.autoconfigure.data.mongo;

import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MongoReactiveDataAutoConfiguration}.
 *
 * @author Mark Paluch
 */
public class MongoReactiveDataAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(
					PropertyPlaceholderAutoConfiguration.class,
					MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
					MongoReactiveAutoConfiguration.class,
					MongoReactiveDataAutoConfiguration.class));

	@Test
	public void templateExists() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(ReactiveMongoTemplate.class));
	}

}
