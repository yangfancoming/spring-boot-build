

package org.springframework.boot.autoconfigure.cache;

import java.util.Collection;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Generic cache configuration based on arbitrary {@link Cache} instances defined in the
 * context.
 *
 * @author Stephane Nicoll
 * @since 1.3.0
 */
@Configuration
@ConditionalOnBean(Cache.class)
@ConditionalOnMissingBean(CacheManager.class)
@Conditional(CacheCondition.class)
class GenericCacheConfiguration {

	private final CacheManagerCustomizers customizers;

	GenericCacheConfiguration(CacheManagerCustomizers customizers) {
		this.customizers = customizers;
	}

	@Bean
	public SimpleCacheManager cacheManager(Collection<Cache> caches) {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(caches);
		return this.customizers.customize(cacheManager);
	}

}
