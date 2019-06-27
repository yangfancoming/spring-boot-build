

package org.springframework.boot.autoconfigure.cache;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfigurationTests.DefaultCacheAndCustomizersConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfigurationTests.DefaultCacheConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfigurationTests.EhCacheCustomCacheManager;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CacheAutoConfigurationTests} with EhCache 2.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("ehcache-3*.jar")
public class EhCache2CacheAutoConfigurationTests
		extends AbstractCacheAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(CacheAutoConfiguration.class));

	@Test
	public void ehCacheWithCaches() {
		this.contextRunner.withUserConfiguration(DefaultCacheConfiguration.class)
				.withPropertyValues("spring.cache.type=ehcache").run((context) -> {
					EhCacheCacheManager cacheManager = getCacheManager(context,
							EhCacheCacheManager.class);
					assertThat(cacheManager.getCacheNames()).containsOnly("cacheTest1",
							"cacheTest2");
					assertThat(context.getBean(net.sf.ehcache.CacheManager.class))
							.isEqualTo(cacheManager.getCacheManager());
				});
	}

	@Test
	public void ehCacheWithCustomizers() {
		this.contextRunner
				.withUserConfiguration(DefaultCacheAndCustomizersConfiguration.class)
				.withPropertyValues("spring.cache.type=" + "ehcache")
				.run(verifyCustomizers("allCacheManagerCustomizer",
						"ehcacheCacheManagerCustomizer"));
	}

	@Test
	public void ehCacheWithConfig() {
		this.contextRunner.withUserConfiguration(DefaultCacheConfiguration.class)
				.withPropertyValues("spring.cache.type=ehcache",
						"spring.cache.ehcache.config=cache/ehcache-override.xml")
				.run((context) -> {
					EhCacheCacheManager cacheManager = getCacheManager(context,
							EhCacheCacheManager.class);
					assertThat(cacheManager.getCacheNames())
							.containsOnly("cacheOverrideTest1", "cacheOverrideTest2");
				});
	}

	@Test
	public void ehCacheWithExistingCacheManager() {
		this.contextRunner.withUserConfiguration(EhCacheCustomCacheManager.class)
				.withPropertyValues("spring.cache.type=ehcache").run((context) -> {
					EhCacheCacheManager cacheManager = getCacheManager(context,
							EhCacheCacheManager.class);
					assertThat(cacheManager.getCacheManager())
							.isEqualTo(context.getBean("customEhCacheCacheManager"));
				});
	}

}
