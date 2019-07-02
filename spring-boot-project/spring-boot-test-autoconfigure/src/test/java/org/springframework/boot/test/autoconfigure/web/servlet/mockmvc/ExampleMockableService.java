

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.stereotype.Service;

/**
 * Example mockable {@link Service} used with {@link WebMvcTest} tests.
 *
 * @author Phillip Webb
 */
@Service
public class ExampleMockableService {

	public ExampleMockableService() {
		throw new IllegalStateException("Should not be called");
	}

}
