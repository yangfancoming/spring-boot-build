

package org.springframework.boot.test.mock.mockito.example;

/**
 * Example service implementation for spy tests.
 *
 * @author Phillip Webb
 */
public class SimpleExampleService extends RealExampleService {

	public SimpleExampleService() {
		super("simple");
	}

}
