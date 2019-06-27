

package org.springframework.boot.autoconfigure.mongo;

import javax.net.SocketFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MongoAutoConfiguration}.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
public class MongoAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MongoAutoConfiguration.class));

	@Test
	public void clientExists() {
		this.contextRunner
				.run((context) -> assertThat(context).hasSingleBean(MongoClient.class));
	}

	@Test
	public void optionsAdded() {
		this.contextRunner.withPropertyValues("spring.data.mongodb.host:localhost")
				.withUserConfiguration(OptionsConfig.class)
				.run((context) -> assertThat(context.getBean(MongoClient.class)
						.getMongoClientOptions().getSocketTimeout()).isEqualTo(300));
	}

	@Test
	public void optionsAddedButNoHost() {
		this.contextRunner
				.withPropertyValues("spring.data.mongodb.uri:mongodb://localhost/test")
				.withUserConfiguration(OptionsConfig.class)
				.run((context) -> assertThat(context.getBean(MongoClient.class)
						.getMongoClientOptions().getSocketTimeout()).isEqualTo(300));
	}

	@Test
	public void optionsSslConfig() {
		this.contextRunner
				.withPropertyValues("spring.data.mongodb.uri:mongodb://localhost/test")
				.withUserConfiguration(SslOptionsConfig.class).run((context) -> {
					assertThat(context).hasSingleBean(MongoClient.class);
					MongoClient mongo = context.getBean(MongoClient.class);
					MongoClientOptions options = mongo.getMongoClientOptions();
					assertThat(options.isSslEnabled()).isTrue();
					assertThat(options.getSocketFactory())
							.isSameAs(context.getBean("mySocketFactory"));
				});
	}

	@Configuration
	static class OptionsConfig {

		@Bean
		public MongoClientOptions mongoOptions() {
			return MongoClientOptions.builder().socketTimeout(300).build();
		}

	}

	@Configuration
	static class SslOptionsConfig {

		@Bean
		public MongoClientOptions mongoClientOptions() {
			return MongoClientOptions.builder().sslEnabled(true)
					.socketFactory(mySocketFactory()).build();
		}

		@Bean
		public SocketFactory mySocketFactory() {
			return mock(SocketFactory.class);
		}

	}

}
