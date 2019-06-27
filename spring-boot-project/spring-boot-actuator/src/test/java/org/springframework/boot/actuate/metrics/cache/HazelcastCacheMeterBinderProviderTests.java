

package org.springframework.boot.actuate.metrics.cache;

import java.util.Collections;

import com.hazelcast.core.IMap;
import com.hazelcast.spring.cache.HazelcastCache;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.cache.HazelcastCacheMetrics;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link HazelcastCacheMeterBinderProvider}.
 *
 * @author Stephane Nicoll
 */
public class HazelcastCacheMeterBinderProviderTests {

	@SuppressWarnings("unchecked")
	@Test
	public void hazelcastCacheProvider() {
		IMap<Object, Object> nativeCache = mock(IMap.class);
		given(nativeCache.getName()).willReturn("test");
		HazelcastCache cache = new HazelcastCache(nativeCache);
		MeterBinder meterBinder = new HazelcastCacheMeterBinderProvider()
				.getMeterBinder(cache, Collections.emptyList());
		assertThat(meterBinder).isInstanceOf(HazelcastCacheMetrics.class);
	}

}
