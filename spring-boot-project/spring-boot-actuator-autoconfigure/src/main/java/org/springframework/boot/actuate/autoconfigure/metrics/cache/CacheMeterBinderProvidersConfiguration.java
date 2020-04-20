

package org.springframework.boot.actuate.autoconfigure.metrics.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.spring.cache.HazelcastCache;
import io.micrometer.core.instrument.binder.MeterBinder;
import net.sf.ehcache.Ehcache;

import org.springframework.boot.actuate.metrics.cache.CacheMeterBinderProvider;
import org.springframework.boot.actuate.metrics.cache.CaffeineCacheMeterBinderProvider;
import org.springframework.boot.actuate.metrics.cache.EhCache2CacheMeterBinderProvider;
import org.springframework.boot.actuate.metrics.cache.HazelcastCacheMeterBinderProvider;
import org.springframework.boot.actuate.metrics.cache.JCacheCacheMeterBinderProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Configuration Auto-configuration} for {@link CacheMeterBinderProvider} beans.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass(MeterBinder.class)
class CacheMeterBinderProvidersConfiguration {

	@Configuration
	@ConditionalOnClass({ CaffeineCache.class,
			com.github.benmanes.caffeine.cache.Cache.class })
	static class CaffeineCacheMeterBinderProviderConfiguration {

		@Bean
		public CaffeineCacheMeterBinderProvider caffeineCacheMeterBinderProvider() {
			return new CaffeineCacheMeterBinderProvider();
		}

	}

	@Configuration
	@ConditionalOnClass({ EhCacheCache.class, Ehcache.class })
	static class EhCache2CacheMeterBinderProviderConfiguration {

		@Bean
		public EhCache2CacheMeterBinderProvider ehCache2CacheMeterBinderProvider() {
			return new EhCache2CacheMeterBinderProvider();
		}

	}

	@Configuration
	@ConditionalOnClass({ HazelcastCache.class, Hazelcast.class })
	static class HazelcastCacheMeterBinderProviderConfiguration {

		@Bean
		public HazelcastCacheMeterBinderProvider hazelcastCacheMeterBinderProvider() {
			return new HazelcastCacheMeterBinderProvider();
		}

	}

	@Configuration
	@ConditionalOnClass({ JCacheCache.class, javax.cache.CacheManager.class })
	static class JCacheCacheMeterBinderProviderConfiguration {

		@Bean
		public JCacheCacheMeterBinderProvider jCacheCacheMeterBinderProvider() {
			return new JCacheCacheMeterBinderProvider();
		}

	}

}
