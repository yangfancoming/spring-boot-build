

package org.springframework.boot.test.mock.mockito.example;

/**
 * Example generic service implementation for spy tests.
 *
 * @author Phillip Webb
 */
public class SimpleExampleStringGenericService implements ExampleGenericService<String> {

	private final String greeting;

	public SimpleExampleStringGenericService() {
		this("simple");
	}

	public SimpleExampleStringGenericService(String greeting) {
		this.greeting = greeting;
	}

	@Override
	public String greeting() {
		return this.greeting;
	}

}
