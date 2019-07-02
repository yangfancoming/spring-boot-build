

package org.springframework.boot.test.context.filter;

import java.io.IOException;

import org.junit.Test;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TestTypeExcludeFilter}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class TestTypeExcludeFilterTests {

	private TestTypeExcludeFilter filter = new TestTypeExcludeFilter();

	private MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

	@Test
	public void matchesJUnit4TestClass() throws Exception {
		assertThat(this.filter.match(getMetadataReader(TestTypeExcludeFilterTests.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesJUnitJupiterTestClass() throws Exception {
		assertThat(this.filter.match(getMetadataReader(JupiterTestExample.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesJUnitJupiterRepeatedTestClass() throws Exception {
		assertThat(this.filter.match(getMetadataReader(JupiterRepeatedTestExample.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesJUnitJupiterTestFactoryClass() throws Exception {
		assertThat(this.filter.match(getMetadataReader(JupiterTestFactoryExample.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesNestedConfiguration() throws Exception {
		assertThat(this.filter.match(getMetadataReader(NestedConfig.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesNestedConfigurationClassWithoutTestMethodsIfItHasRunWith()
			throws Exception {
		assertThat(this.filter.match(
				getMetadataReader(AbstractTestWithConfigAndRunWith.Config.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesNestedConfigurationClassWithoutTestMethodsIfItHasExtendWith()
			throws Exception {
		assertThat(this.filter.match(
				getMetadataReader(
						AbstractJupiterTestWithConfigAndExtendWith.Config.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void matchesTestConfiguration() throws Exception {
		assertThat(this.filter.match(getMetadataReader(SampleTestConfig.class),
				this.metadataReaderFactory)).isTrue();
	}

	@Test
	public void doesNotMatchRegularConfiguration() throws Exception {
		assertThat(this.filter.match(getMetadataReader(SampleConfig.class),
				this.metadataReaderFactory)).isFalse();
	}

	private MetadataReader getMetadataReader(Class<?> source) throws IOException {
		return this.metadataReaderFactory.getMetadataReader(source.getName());
	}

	@Configuration
	static class NestedConfig {

	}

}