

package org.springframework.boot.test.mock.mockito;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for a mock bean where the class being mocked uses field injection.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
public class MockBeanWithInjectedFieldIntegrationTests {

	@MockBean
	private MyService myService;

	@Test
	public void fieldInjectionIntoMyServiceMockIsNotAttempted() {
		given(this.myService.getCount()).willReturn(5);
		assertThat(this.myService.getCount()).isEqualTo(5);
	}

	private static class MyService {

		@Autowired
		private MyRepository repository;

		public int getCount() {
			return this.repository.findAll().size();
		}

	}

	private interface MyRepository {

		List<Object> findAll();

	}

}
