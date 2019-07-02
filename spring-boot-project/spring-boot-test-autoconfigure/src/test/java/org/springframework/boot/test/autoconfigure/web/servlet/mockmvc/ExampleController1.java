

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example {@link Controller} used with {@link WebMvcTest} tests.
 *
 * @author Phillip Webb
 */
@RestController
public class ExampleController1 {

	@GetMapping("/one")
	public String one() {
		return "one";
	}

	@GetMapping("/error")
	public String error() {
		throw new ExampleException();
	}

	@GetMapping(path = "/html", produces = "text/html")
	public String html() {
		return "<html><body>Hello</body></html>";
	}

}
