

package org.springframework.boot.actuate.metrics.cache;

import com.hazelcast.spring.cache.HazelcastCache;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.cache.HazelcastCacheMetrics;

/**
 * {@link CacheMeterBinderProvider} implementation for Hazelcast.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class HazelcastCacheMeterBinderProvider
		implements CacheMeterBinderProvider<HazelcastCache> {

	@Override
	public MeterBinder getMeterBinder(HazelcastCache cache, Iterable<Tag> tags) {
		return new HazelcastCacheMetrics(cache.getNativeCache(), tags);
	}

}
