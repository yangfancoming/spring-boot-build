

package org.springframework.boot.actuate.autoconfigure.metrics.cache;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import org.springframework.boot.actuate.metrics.cache.CacheMeterBinderProvider;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Configure a {@link CacheMetricsRegistrar} and register all available {@link Cache
 * caches}.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnBean({ CacheMeterBinderProvider.class, MeterRegistry.class })
class CacheMetricsRegistrarConfiguration {

	private static final String CACHE_MANAGER_SUFFIX = "cacheManager";

	private final MeterRegistry registry;

	private final Collection<CacheMeterBinderProvider<?>> binderProviders;

	private final Map<String, CacheManager> cacheManagers;

	CacheMetricsRegistrarConfiguration(MeterRegistry registry,
			Collection<CacheMeterBinderProvider<?>> binderProviders,
			Map<String, CacheManager> cacheManagers) {
		this.registry = registry;
		this.binderProviders = binderProviders;
		this.cacheManagers = cacheManagers;
	}

	@Bean
	public CacheMetricsRegistrar cacheMetricsRegistrar() {
		return new CacheMetricsRegistrar(this.registry, this.binderProviders);
	}

	@PostConstruct
	public void bindCachesToRegistry() {
		this.cacheManagers.forEach(this::bindCacheManagerToRegistry);
	}

	private void bindCacheManagerToRegistry(String beanName, CacheManager cacheManager) {
		cacheManager.getCacheNames().forEach((cacheName) -> bindCacheToRegistry(beanName,
				cacheManager.getCache(cacheName)));
	}

	private void bindCacheToRegistry(String beanName, Cache cache) {
		Tag cacheManagerTag = Tag.of("cacheManager", getCacheManagerName(beanName));
		cacheMetricsRegistrar().bindCacheToRegistry(cache, cacheManagerTag);
	}

	/**
	 * Get the name of a {@link CacheManager} based on its {@code beanName}.
	 * @param beanName the name of the {@link CacheManager} bean
	 * @return a name for the given cache manager
	 */
	private String getCacheManagerName(String beanName) {
		if (beanName.length() > CACHE_MANAGER_SUFFIX.length()
				&& StringUtils.endsWithIgnoreCase(beanName, CACHE_MANAGER_SUFFIX)) {
			return beanName.substring(0,
					beanName.length() - CACHE_MANAGER_SUFFIX.length());
		}
		return beanName;
	}

}
