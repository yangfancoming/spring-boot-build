

package org.springframework.boot.autoconfigure.data.couchbase;

import javax.validation.Validator;

import com.couchbase.client.java.Bucket;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Couchbase support.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @since 1.4.0
 */
@Configuration
@ConditionalOnClass({ Bucket.class, CouchbaseRepository.class })
@AutoConfigureAfter({ CouchbaseAutoConfiguration.class,
		ValidationAutoConfiguration.class })
@EnableConfigurationProperties(CouchbaseDataProperties.class)
@Import({ CouchbaseConfigurerAdapterConfiguration.class,
		SpringBootCouchbaseDataConfiguration.class })
public class CouchbaseDataAutoConfiguration {

	@Configuration
	@ConditionalOnClass(Validator.class)
	public static class ValidationConfiguration {

		@Bean
		@ConditionalOnSingleCandidate(Validator.class)
		public ValidatingCouchbaseEventListener validationEventListener(
				Validator validator) {
			return new ValidatingCouchbaseEventListener(validator);
		}

	}

}
