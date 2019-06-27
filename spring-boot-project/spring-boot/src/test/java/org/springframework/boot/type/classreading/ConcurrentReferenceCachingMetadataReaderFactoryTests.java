

package org.springframework.boot.type.classreading;

import org.junit.Test;

import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ConcurrentReferenceCachingMetadataReaderFactory}.
 *
 * @author Phillip Webb
 */
public class ConcurrentReferenceCachingMetadataReaderFactoryTests {

	@Test
	public void getMetadataReaderUsesCache() throws Exception {
		TestConcurrentReferenceCachingMetadataReaderFactory factory = spy(
				new TestConcurrentReferenceCachingMetadataReaderFactory());
		MetadataReader metadataReader1 = factory.getMetadataReader(getClass().getName());
		MetadataReader metadataReader2 = factory.getMetadataReader(getClass().getName());
		assertThat(metadataReader1).isSameAs(metadataReader2);
		verify(factory, times(1)).createMetadataReader(any(Resource.class));
	}

	@Test
	public void clearResetsCache() throws Exception {
		TestConcurrentReferenceCachingMetadataReaderFactory factory = spy(
				new TestConcurrentReferenceCachingMetadataReaderFactory());
		MetadataReader metadataReader1 = factory.getMetadataReader(getClass().getName());
		factory.clearCache();
		MetadataReader metadataReader2 = factory.getMetadataReader(getClass().getName());
		assertThat(metadataReader1).isNotEqualTo(sameInstance(metadataReader2));
		verify(factory, times(2)).createMetadataReader(any(Resource.class));
	}

	private static class TestConcurrentReferenceCachingMetadataReaderFactory
			extends ConcurrentReferenceCachingMetadataReaderFactory {

		@Override
		public MetadataReader createMetadataReader(Resource resource) {
			return mock(MetadataReader.class);
		}

	}

}
