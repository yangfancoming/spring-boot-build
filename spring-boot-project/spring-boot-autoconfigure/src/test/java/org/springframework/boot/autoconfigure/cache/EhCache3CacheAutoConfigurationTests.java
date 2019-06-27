

package org.springframework.boot.autoconfigure.cache;

import org.ehcache.jsr107.EhcacheCachingProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.cache.CacheAutoConfigurationTests.DefaultCacheConfiguration;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CacheAutoConfigurationTests} with EhCache 3.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("ehcache-2*.jar")
public class EhCache3CacheAutoConfigurationTests
		extends AbstractCacheAutoConfigurationTests {

	@Test
	public void ehcache3AsJCacheWithCaches() {
		String cachingProviderFqn = EhcacheCachingProvider.class.getName();
		this.contextRunner.withUserConfiguration(DefaultCacheConfiguration.class)
				.withPropertyValues("spring.cache.type=jcache",
						"spring.cache.jcache.provider=" + cachingProviderFqn,
						"spring.cache.cacheNames[0]=foo",
						"spring.cache.cacheNames[1]=bar")
				.run((context) -> {
					JCacheCacheManager cacheManager = getCacheManager(context,
							JCacheCacheManager.class);
					assertThat(cacheManager.getCacheNames()).containsOnly("foo", "bar");
				});
	}

	@Test
	public void ehcache3AsJCacheWithConfig() {
		String cachingProviderFqn = EhcacheCachingProvider.class.getName();
		String configLocation = "ehcache3.xml";
		this.contextRunner.withUserConfiguration(DefaultCacheConfiguration.class)
				.withPropertyValues("spring.cache.type=jcache",
						"spring.cache.jcache.provider=" + cachingProviderFqn,
						"spring.cache.jcache.config=" + configLocation)
				.run((context) -> {
					JCacheCacheManager cacheManager = getCacheManager(context,
							JCacheCacheManager.class);

					Resource configResource = new ClassPathResource(configLocation);
					assertThat(cacheManager.getCacheManager().getURI())
							.isEqualTo(configResource.getURI());
					assertThat(cacheManager.getCacheNames()).containsOnly("foo", "bar");
				});
	}

}
