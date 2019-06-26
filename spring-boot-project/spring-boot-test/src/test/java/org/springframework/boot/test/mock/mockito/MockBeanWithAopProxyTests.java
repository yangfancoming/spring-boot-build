

package org.springframework.boot.test.mock.mockito;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test {@link MockBean} when mixed with Spring AOP.
 *
 * @author Phillip Webb
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/5837">5837</a>
 */
@RunWith(SpringRunner.class)
public class MockBeanWithAopProxyTests {

	@MockBean
	private DateService dateService;

	@Test
	public void verifyShouldUseProxyTarget() {
		given(this.dateService.getDate(false)).willReturn(1L);
		Long d1 = this.dateService.getDate(false);
		assertThat(d1).isEqualTo(1L);
		given(this.dateService.getDate(false)).willReturn(2L);
		Long d2 = this.dateService.getDate(false);
		assertThat(d2).isEqualTo(2L);
		verify(this.dateService, times(2)).getDate(false);
		verify(this.dateService, times(2)).getDate(eq(false));
		verify(this.dateService, times(2)).getDate(anyBoolean());
	}

	@Configuration
	@EnableCaching(proxyTargetClass = true)
	@Import(DateService.class)
	static class Config {

		@Bean
		public CacheResolver cacheResolver(CacheManager cacheManager) {
			SimpleCacheResolver resolver = new SimpleCacheResolver();
			resolver.setCacheManager(cacheManager);
			return resolver;
		}

		@Bean
		public ConcurrentMapCacheManager cacheManager() {
			ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
			cacheManager.setCacheNames(Arrays.asList("test"));
			return cacheManager;
		}

	}

	@Service
	static class DateService {

		@Cacheable(cacheNames = "test")
		public Long getDate(boolean argument) {
			return System.nanoTime();
		}

	}

}
