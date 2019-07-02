

package org.springframework.boot.test.autoconfigure.data.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test with custom include filter for {@link DataMongoTest}.
 *
 * @author Michael Simons
 */
@RunWith(SpringRunner.class)
@DataMongoTest(includeFilters = @Filter(Service.class))
public class DataMongoTestWithIncludeFilterIntegrationTests {

	@Autowired
	private ExampleService service;

	@Test
	public void testService() {
		assertThat(this.service.hasCollection("foobar")).isFalse();
	}

}
