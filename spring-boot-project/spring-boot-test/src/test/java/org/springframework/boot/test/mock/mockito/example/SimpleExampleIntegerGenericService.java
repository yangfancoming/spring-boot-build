

package org.springframework.boot.test.mock.mockito.example;

/**
 * Example generic service implementation for spy tests.
 *
 * @author Phillip Webb
 */
public class SimpleExampleIntegerGenericService
		implements ExampleGenericService<Integer> {

	@Override
	public Integer greeting() {
		return 123;
	}

}
