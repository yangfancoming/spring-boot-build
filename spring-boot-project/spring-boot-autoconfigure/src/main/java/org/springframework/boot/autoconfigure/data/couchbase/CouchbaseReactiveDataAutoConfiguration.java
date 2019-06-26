

package org.springframework.boot.autoconfigure.data.couchbase;

import com.couchbase.client.java.Bucket;
import reactor.core.publisher.Flux;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Reactive Couchbase
 * support.
 *
 * @author Alex Derkach
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass({ Bucket.class, ReactiveCouchbaseRepository.class, Flux.class })
@AutoConfigureAfter(CouchbaseDataAutoConfiguration.class)
@Import(SpringBootCouchbaseReactiveDataConfiguration.class)
public class CouchbaseReactiveDataAutoConfiguration {

}
