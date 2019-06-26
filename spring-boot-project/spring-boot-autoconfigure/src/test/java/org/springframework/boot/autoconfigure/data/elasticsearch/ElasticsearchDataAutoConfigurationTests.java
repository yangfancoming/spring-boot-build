

package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ElasticsearchDataAutoConfiguration}.
 *
 * @author Phillip Webb
 * @author Artur Konczak
 */
public class ElasticsearchDataAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void templateBackOffWithNoClient() {
		this.context = new AnnotationConfigApplicationContext(
				ElasticsearchDataAutoConfiguration.class);
		assertThat(this.context.getBeansOfType(ElasticsearchTemplate.class)).isEmpty();
	}

	@Test
	public void templateExists() {
		this.context = new AnnotationConfigApplicationContext();
		new ElasticsearchNodeTemplate().doWithNode((node) -> {
			TestPropertyValues
					.of("spring.data.elasticsearch.properties.path.data:target/data",
							"spring.data.elasticsearch.properties.path.logs:target/logs",
							"spring.data.elasticsearch.cluster-nodes:localhost:"
									+ node.getTcpPort())
					.applyTo(this.context);
			this.context.register(PropertyPlaceholderAutoConfiguration.class,
					ElasticsearchAutoConfiguration.class,
					ElasticsearchDataAutoConfiguration.class);
			this.context.refresh();
			assertHasSingleBean(ElasticsearchTemplate.class);
		});
	}

	@Test
	public void mappingContextExists() {
		this.context = new AnnotationConfigApplicationContext();
		new ElasticsearchNodeTemplate().doWithNode((node) -> {
			TestPropertyValues
					.of("spring.data.elasticsearch.properties.path.data:target/data",
							"spring.data.elasticsearch.properties.path.logs:target/logs",
							"spring.data.elasticsearch.cluster-nodes:localhost:"
									+ node.getTcpPort())
					.applyTo(this.context);
			this.context.register(PropertyPlaceholderAutoConfiguration.class,
					ElasticsearchAutoConfiguration.class,
					ElasticsearchDataAutoConfiguration.class);
			this.context.refresh();
			assertHasSingleBean(SimpleElasticsearchMappingContext.class);
		});
	}

	@Test
	public void converterExists() {
		this.context = new AnnotationConfigApplicationContext();
		new ElasticsearchNodeTemplate().doWithNode((node) -> {
			TestPropertyValues
					.of("spring.data.elasticsearch.properties.path.data:target/data",
							"spring.data.elasticsearch.properties.path.logs:target/logs",
							"spring.data.elasticsearch.cluster-nodes:localhost:"
									+ node.getTcpPort())
					.applyTo(this.context);
			this.context.register(PropertyPlaceholderAutoConfiguration.class,
					ElasticsearchAutoConfiguration.class,
					ElasticsearchDataAutoConfiguration.class);
			this.context.refresh();
			assertHasSingleBean(ElasticsearchConverter.class);
		});
	}

	private void assertHasSingleBean(Class<?> type) {
		assertThat(this.context.getBeanNamesForType(type)).hasSize(1);
	}

}
