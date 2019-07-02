

package org.springframework.boot.test.autoconfigure.data.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Example repository used with {@link DataMongoTest} tests.
 *
 * @author Michael Simons
 */
public interface ExampleRepository extends MongoRepository<ExampleDocument, String> {

}
