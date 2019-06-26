

package org.springframework.boot.autoconfigure.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

/**
 * Infinispan cache configuration.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Raja Kolli
 * @since 1.3.0
 */
@Configuration
@ConditionalOnClass(SpringEmbeddedCacheManager.class)
@ConditionalOnMissingBean(CacheManager.class)
@Conditional(CacheCondition.class)
public class InfinispanCacheConfiguration {

	private final CacheProperties cacheProperties;

	private final CacheManagerCustomizers customizers;

	private final ConfigurationBuilder defaultConfigurationBuilder;

	public InfinispanCacheConfiguration(CacheProperties cacheProperties,
			CacheManagerCustomizers customizers,
			ObjectProvider<ConfigurationBuilder> defaultConfigurationBuilder) {
		this.cacheProperties = cacheProperties;
		this.customizers = customizers;
		this.defaultConfigurationBuilder = defaultConfigurationBuilder.getIfAvailable();
	}

	@Bean
	public SpringEmbeddedCacheManager cacheManager(
			EmbeddedCacheManager embeddedCacheManager) {
		SpringEmbeddedCacheManager cacheManager = new SpringEmbeddedCacheManager(
				embeddedCacheManager);
		return this.customizers.customize(cacheManager);
	}

	@Bean(destroyMethod = "stop")
	@ConditionalOnMissingBean
	public EmbeddedCacheManager infinispanCacheManager() throws IOException {
		EmbeddedCacheManager cacheManager = createEmbeddedCacheManager();
		List<String> cacheNames = this.cacheProperties.getCacheNames();
		if (!CollectionUtils.isEmpty(cacheNames)) {
			cacheNames.forEach((cacheName) -> cacheManager.defineConfiguration(cacheName,
					getDefaultCacheConfiguration()));
		}
		return cacheManager;
	}

	private EmbeddedCacheManager createEmbeddedCacheManager() throws IOException {
		Resource location = this.cacheProperties
				.resolveConfigLocation(this.cacheProperties.getInfinispan().getConfig());
		if (location != null) {
			try (InputStream in = location.getInputStream()) {
				return new DefaultCacheManager(in);
			}
		}
		return new DefaultCacheManager();
	}

	private org.infinispan.configuration.cache.Configuration getDefaultCacheConfiguration() {
		if (this.defaultConfigurationBuilder != null) {
			return this.defaultConfigurationBuilder.build();
		}
		return new ConfigurationBuilder().build();
	}

}
