

package org.springframework.boot.test.autoconfigure.core;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AutoConfigureCache} with an existing {@link CacheManager}.
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureCache
public class AutoConfigureCacheWithExistingCacheManagerIntegrationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void shouldNotReplaceExistingCacheManager() {
		CacheManager bean = this.applicationContext.getBean(CacheManager.class);
		assertThat(bean).isInstanceOf(ConcurrentMapCacheManager.class);
	}

	@Configuration
	@EnableCaching
	public static class Config {

		@Bean
		public ConcurrentMapCacheManager existingCacheManager() {
			return new ConcurrentMapCacheManager();
		}

	}

}
