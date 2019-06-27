

package org.springframework.boot.autoconfigure.data.elasticsearch;

import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ElasticsearchAutoConfiguration}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class ElasticsearchAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void useExistingClient() {
		this.context = new AnnotationConfigApplicationContext();
		this.context.register(CustomConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class,
				ElasticsearchAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeanNamesForType(Client.class).length).isEqualTo(1);
		assertThat(this.context.getBean("myClient"))
				.isSameAs(this.context.getBean(Client.class));
	}

	@Test
	public void createTransportClient() {
		this.context = new AnnotationConfigApplicationContext();
		new ElasticsearchNodeTemplate().doWithNode((node) -> {
			TestPropertyValues.of(
					"spring.data.elasticsearch.cluster-nodes:localhost:"
							+ node.getTcpPort(),
					"spring.data.elasticsearch.properties.path.home:target/es/client")
					.applyTo(this.context);
			this.context.register(PropertyPlaceholderAutoConfiguration.class,
					ElasticsearchAutoConfiguration.class);
			this.context.refresh();
			List<DiscoveryNode> connectedNodes = this.context
					.getBean(TransportClient.class).connectedNodes();
			assertThat(connectedNodes).hasSize(1);
		});
	}

	@Configuration
	static class CustomConfiguration {

		@Bean
		public Client myClient() {
			return mock(Client.class);
		}

	}

}
