

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Example {@link Controller} used with {@link WebMvcTest} tests.
 *
 * @author Phillip Webb
 */
@Controller
public class ExampleController2 {

	@GetMapping("/two")
	@ResponseBody
	public String two(ExampleArgument argument) {
		return argument + "two";
	}

	@GetMapping("/two/{id}")
	@ResponseBody
	public String two(@PathVariable ExampleId id) {
		return id.getId() + "two";
	}

}
