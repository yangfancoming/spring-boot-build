

package org.springframework.boot.test.mock.mockito.example;

/**
 * Example service interface for mocking tests.
 *
 * @param <T> the generic type
 * @author Phillip Webb
 */
public interface ExampleGenericService<T> {

	T greeting();

}
