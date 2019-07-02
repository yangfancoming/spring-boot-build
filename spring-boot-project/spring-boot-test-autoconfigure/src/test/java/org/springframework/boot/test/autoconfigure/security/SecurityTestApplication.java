

package org.springframework.boot.test.autoconfigure.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcSecurityAutoConfiguration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tests application for {@link MockMvcSecurityAutoConfiguration}.
 *
 * @author Andy Wilkinson
 */
@SpringBootApplication
public class SecurityTestApplication {

	@RestController
	static class MyController {

		@RequestMapping("/")
		@Secured("ROLE_USER")
		public String index() {
			return "Hello";
		}

	}

}
